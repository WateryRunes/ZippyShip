package game1;

import utilities.JEasyFrame;
import utilities.Vector2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static game1.Asteroid.HORIZONTAL_MAX_SPEED;
import static game1.Asteroid.ASTEROID_RADIUS;
import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;
import static game1.Particle.PARTICLE_SPEED;

// flappy birds style
// basic:
//   ship
//   obstacles
//   3 lives (spawn on left side of screen when line of asteroids just hit it)
// advanced:
//   visual + sound effects - particles
//   power ups - mining via collision, temporary gun, score inc
//   high scores? why not

// bugs
//   need random particle directions!!!!!!
//   high scores? fairly easy?
//   splat needs to shoot in 3 directions

// if i have time/learn how
//   more sounds + better visuals for powerups/ships

public class Game {
    public long lineSpawnTime;
    public int lineIntervalTime = 5000;
    public long extraSpawnTime;
    public long scoreGainedTime;
    public List<GameObject> objects;
    private Keys ctrl;
    public Ship ship;
    public EnemyShip enemyShip = null;
    public boolean gameEnded = false;
    public ArrayList<String> highScoresToDisplay = new ArrayList<>();
    private int score;
    public int lives = 3;
    public int noPowerUps = 0;
    public long powerUpSpawnTime;
    public ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    public ArrayList<Particle> particles = new ArrayList<>();
    public Dictionary<String, ArrayList<String>> canDestroy = new Hashtable<>();
    public ArrayList<Score> highScoresMap;

    // constructor of game, initialises what objects can destroy, keyboard listener and starts levels
    public Game() {
        objects = new ArrayList<GameObject>();
        ctrl = new Keys();
        canDestroy.put("SHIP", new ArrayList<String>(Arrays.asList("POWERUP")));
        canDestroy.put("ASTEROID", new ArrayList<String>(Arrays.asList("SHIP", "BULLET", "CANNON", "SPLAT")));
        canDestroy.put("BULLET", new ArrayList<String>(Arrays.asList("SHIP", "CANNON", "SPLAT")));
        canDestroy.put("POWERUP", new ArrayList<String>());
        canDestroy.put("CANNON", new ArrayList<String>(Arrays.asList("SHIP")));
        canDestroy.put("SPLAT", new ArrayList<String>(Arrays.asList("SHIP")));
        startLevel();
    }

    // MAIN, creates Game and View object and while true to keep game running
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        View view = new View(game);
        new JEasyFrame(view, "ZippyShip").addKeyListener(game.ctrl);
        // run the game
        while (true) {
            game.update();
            view.repaint();
            Thread.sleep(Constants.DELAY);
        }
    }

    // creates new line of asteroids every lineIntervalTime
    public void createNewAsteroids(){
        int skip = (int) (Math.random() * FRAME_HEIGHT / (ASTEROID_RADIUS * 2));
        for(int i = 0; i < FRAME_HEIGHT / (ASTEROID_RADIUS * 2); i++){
            if(i != skip){
                objects.add(new Asteroid(this, FRAME_WIDTH + ASTEROID_RADIUS, i * ASTEROID_RADIUS * 2 + ASTEROID_RADIUS, HORIZONTAL_MAX_SPEED, 0, ASTEROID_RADIUS));
            }
        }
        lineSpawnTime = System.currentTimeMillis();
    }

    // starts game with ship and asteroids, starts timer for when powerups/enemyships can spawn
    public void startLevel(){
        synchronized(Game.class) {
            createNewAsteroids();
            powerUpSpawnTime = System.currentTimeMillis();
            extraSpawnTime = System.currentTimeMillis() + (lineIntervalTime / 2);
            ship = new Ship(ctrl, this);
            objects.add(ship);
        }
    }

    // part of game loop, will update objects every 0.02s
    public void update() throws InterruptedException {
        // stores all alive objects
        List<GameObject> alive = new ArrayList<>();

        // update, draw and handle objects. if die, cause explosion particles
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).update();

            for(int j = i + 1; j < objects.size(); j++){
                objects.get(i).collisionHandling(objects.get(j));
            }

            if(!objects.get(i).GO_dead){
                alive.add(objects.get(i));
            }
            else{
                explode(objects.get(i));
            }
        }

        // draw and update particles
        Iterator<Particle> particleIter = particles.iterator();
        while(particleIter.hasNext()){
            Particle p = particleIter.next();
            p.update();
            if(System.currentTimeMillis() - p.creationTime > p.lifetime){
                p.GO_dead = true;
                particleIter.remove();
            }
        }

        // add ship bullet to alive if ship shoots bullet
        if(ship.bullet != null){
            alive.add(ship.bullet);
            ship.bullet = null;
        }

        // add enemy ship bullets to alive when they shoot
        if(enemyShip != null){
            if(!enemyShip.bullet.isEmpty()){
                alive.addAll(enemyShip.bullet);
                enemyShip.bullet.clear();
            }
        }

        // draws and updates powerups, deactivates when time runs out
        Iterator<PowerUp> powerupsIter = activePowerUps.iterator();
        while(powerupsIter.hasNext()){
            PowerUp p = powerupsIter.next();
            if(System.currentTimeMillis() - p.activateTime > p.lifetime){
                deactivatePowerUp(p);
                powerupsIter.remove();
            }
        }

        // lose a life/end game if necessary when ship dies
        if(ship.GO_dead){
            synchronized (Game.class) {
                if(System.currentTimeMillis() - scoreGainedTime < 500) {
                    if (lives > 0) {
                        ship = new Ship(ctrl, this);
                        alive.add(ship);
                        lives--;
                    } else {
                        if(!gameEnded) {
                            System.out.println("Score: " + score);
                            endGame();
                        }
                    }
                }
            }
        }

        // add all 'alive' objects into new object list. create new asteroids/powerups/enemyships if necessary time has passed/conditions are met
        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
            if(System.currentTimeMillis() - lineSpawnTime > lineIntervalTime){
               createNewAsteroids();
            }
            if(noPowerUps < 3 && score > 200 && System.currentTimeMillis() - powerUpSpawnTime > 10000){
                objects.add(PowerUp.makePowerUp(this));
                powerUpSpawnTime = System.currentTimeMillis();
                noPowerUps++;
            }
            if(score > 100 && System.currentTimeMillis() - lineSpawnTime > lineIntervalTime * 0.4 && System.currentTimeMillis() - lineSpawnTime < lineIntervalTime * 0.6 && enemyShip == null){
                enemyShip = (EnemyShip.makeEnemyShip(this));
                objects.add(enemyShip);
                extraSpawnTime = System.currentTimeMillis();
            }
        }
    }

    public int getScore(){
        return score;
    }

    public void incScore(int value){
        if(!ship.GO_dead) {
            score += value;
        }
        scoreGainedTime = System.currentTimeMillis();
    }

    // causes different effects depending on powerup picked up
    public void activatePowerUp(PowerUp powerUp){
        if(powerUp.type == PowerUpType.GUN){
            activePowerUps.add(powerUp);
            ship.canShoot = true;
        }
        else if(powerUp.type == PowerUpType.INCSCORE){
            incScore(500);
        }
        else if(powerUp.type == PowerUpType.MINER){
            activePowerUps.add(powerUp);
            canDestroy.get("SHIP").add("ASTEROID");
            canDestroy.get("ASTEROID").remove("SHIP");
        }
    }

    // decatives powerup after time has run out
    public void deactivatePowerUp(PowerUp powerUp){
        if(powerUp.type == PowerUpType.GUN){
            ship.canShoot = false;
        }
        else if(powerUp.type == PowerUpType.MINER){
            canDestroy.get("ASTEROID").add("SHIP");
            canDestroy.get("SHIP").remove("ASTEROID");
        }
    }

    public Vector2D getRandomVelocity() {
        //return new Vector2D(Math.random() * 3, Math.random() * 3);
        return Vector2D.polar(Math.random() * 2 * Math.PI, Math.abs(Constants.RANDOM.nextGaussian() * PARTICLE_SPEED));
    }

    // create 20 particles moving in random directions from point of death of game object, with the gameobjects colour
    public void explode(GameObject object){
        if(object.GO_explodeColor != null){
            for(int i = 0; i < 20; i++){
                particles.add(new Particle(this, new Vector2D(object.GO_position), object.GO_explodeColor));
            }
        }
    }

    // ends game by dealing with (read/writing) high scores and displaying high scores
    public void endGame() throws InterruptedException {
        gameEnded = true;
        highScoresMap = new ArrayList<Score>();
        File f = new File("src\\game1\\highscores.txt"); // gets file object of dataset
        try {
            Scanner r = new Scanner(f); // opens read stream to file
            for(int i = 0; i < 5; i++){
                String[] line = r.nextLine().split(",");
                highScoresMap.add(new Score(Integer.parseInt(line[0]), line[1], true));
            }
            r.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }

        System.out.println("Enter your name to save the highscore: (or type 'no' to not save)");

        Scanner in = new Scanner(System.in);
        String name = in.next();
        if(name.equalsIgnoreCase("no")){
            System.exit(0);
        }
        else {
            System.out.println();
            highScoresMap.add(new Score(score, name, false));
        }
        in.close();

        try {
            FileWriter w = new FileWriter(f);
            Collections.sort(highScoresMap);
            for(int i = 0; i < 5; i++){
                Score s = highScoresMap.get(i);
                w.write(s.score + "," + s.name + "\n");
                highScoresToDisplay.add(s.name + ": " + s.score);
            }
            System.out.println("Saved!");
            w.close();
        }
        catch(IOException e){
            System.out.println("Problem writing to file");
        }
    }
}