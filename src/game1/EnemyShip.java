package game1;

import utilities.Vector2D;

import java.util.ArrayList;

import static game1.Asteroid.HORIZONTAL_MAX_SPEED;
import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;

public abstract class EnemyShip extends GameObject{
    public static final int ENEMYSHIP_RADIUS = 12;
    public Vector2D shootDirection;
    public long lastShotTime;
    public int shotInterval = 2000;
    public ArrayList<Bullet> bullet = new ArrayList<>();

    public EnemyShip(Game game, Vector2D position, Vector2D shootDirection){ // parent class for Cannon and Splat
        super(game, position, new Vector2D(HORIZONTAL_MAX_SPEED, 0), ENEMYSHIP_RADIUS);
        this.shootDirection = shootDirection;
    }

    public static EnemyShip makeEnemyShip(Game game){ // 50/50 on splat or cannon
        if(Math.random() * 2 < 1) {
            if (Math.random() * 2 < 1) { // spawn Cannon on top
                return new Cannon(game, new Vector2D(FRAME_WIDTH + ENEMYSHIP_RADIUS, 50), new Vector2D(0, 1));
            } else { // spawn Splat on bottom
                return new Cannon(game, new Vector2D(FRAME_WIDTH + ENEMYSHIP_RADIUS, FRAME_HEIGHT - 50), new Vector2D(0, -1));
            }
        }
        else {
            return new Splat(game, new Vector2D(FRAME_WIDTH + (ENEMYSHIP_RADIUS * 2), FRAME_HEIGHT / 2), new Vector2D(0, 1));
        }
    }

    @Override
    public void update(){
        super.update();
        if(GO_position.x < 0){
            GO_dead = true;
            game.enemyShip = null;
        }
        if(System.currentTimeMillis() - lastShotTime > shotInterval){
            shoot();
            lastShotTime = System.currentTimeMillis();
        }
    }

    public abstract void shoot();
}
