package game1;

import utilities.SoundManager;
import utilities.Vector2D;

import static game1.Constants.DT;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Asteroid extends GameObject{
    public static final double HORIZONTAL_MAX_SPEED = -80;
    public static final int ASTEROID_RADIUS = 24;
    public Image AsteroidImage = Constants.ASTEROID1;
    public static final Color explodeColor = Color.GRAY;

    public Asteroid(Game game, double x, double y, double vx, double vy, int radius) {
        super(game, new Vector2D(x, y), new Vector2D(vx, vy), radius);
        GO_NAME = "ASTEROID";
        GO_explodeColor = explodeColor;
    }

    public void update() {
        super.update();
        if(GO_position.x < -GO_radius){
            GO_dead = true;
            game.incScore(10);
        }
    }

    public void draw(Graphics2D g) {
        double imW = AsteroidImage.getWidth(null);
        double imH = AsteroidImage.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(GO_velocity.angle(), 0, 0);
        t.scale((GO_radius * 2) / imW, (GO_radius * 2) / imH);
        t.translate(-imW / 2.0, -imH / 2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(GO_position.x, GO_position.y);
        g.drawImage(AsteroidImage, t, null);
        g.setTransform(t0);
    }

    @Override
    public void hit(){
        super.hit();
        game.incScore(20); // gives score in event ship using miner powerup on it
        SoundManager.asteroids();
    }
}