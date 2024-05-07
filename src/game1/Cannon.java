package game1;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;

import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;
import static game1.Ship.MUZZLE_VELOCITY;

public class Cannon extends EnemyShip{ // shoots single bullet in vertical direction depending on which side of the screen it spawns
    public static final Color explodeColor = Color.RED;

    public Cannon(Game game, Vector2D position, Vector2D shootDirection){
        super(game, position, shootDirection);
        GO_NAME = "CANNON";
        GO_explodeColor = explodeColor;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) GO_position.x, (int) GO_position.y, ENEMYSHIP_RADIUS * 2, ENEMYSHIP_RADIUS * 2);
    }

    public void shoot(){
        Vector2D bulletPosition = new Vector2D(GO_position.x + ENEMYSHIP_RADIUS, GO_position.y + ENEMYSHIP_RADIUS);
        bulletPosition.addScaled(shootDirection, GO_radius * 2);
        Vector2D bulletVelocity = new Vector2D(GO_velocity);
        bulletVelocity.addScaled(shootDirection, MUZZLE_VELOCITY);
        bullet.add(new Bullet(game, bulletPosition, bulletVelocity, false));
        SoundManager.fire();
    }
}
