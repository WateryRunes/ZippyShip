package game1;

import utilities.Vector2D;

import java.awt.*;

public class Particle extends GameObject {
    public static final int PARTICLE_RADIUS = 1;
    public static final int PARTICLE_SPEED = 10;
    public Color colour;
    public long creationTime;
    public int lifetime = 2000;

    public Particle(Game game, Vector2D position, Color colour){
        super(game, position, game.getRandomVelocity(), PARTICLE_RADIUS);
        this.colour = colour;
        GO_NAME = "PARTICLE";
        creationTime = System.currentTimeMillis();
    }

    @Override
    public void update(){
        super.update();
    }

    // dont want particles to handle collisions to reduce lag as not necessary
    @Override
    public void collisionHandling(GameObject other){

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(colour);
        g.drawOval((int) GO_position.x, (int) GO_position.y, PARTICLE_RADIUS * 2, PARTICLE_RADIUS * 2);
    }
}
