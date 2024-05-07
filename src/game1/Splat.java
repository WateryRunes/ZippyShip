package game1;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;

import static game1.Ship.MUZZLE_VELOCITY;

public class Splat extends EnemyShip{
    public static final Color explodeColor = Color.RED;
    public int multiplier = 1;

    public Splat(Game game, Vector2D position, Vector2D shootDirection) {
        super(game, position, shootDirection);
        GO_NAME = "SPLAT";
        GO_explodeColor = explodeColor;
        shotInterval = 3000;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillOval((int) GO_position.x, (int) GO_position.y, ENEMYSHIP_RADIUS * 2, ENEMYSHIP_RADIUS * 2);
    }

    public void shoot(){
        multiplier *= -1; // flips shooting direction each shot
        for(int i = 0; i < 3; i++){ // shoots 3 bullets at once
            Vector2D bulletPosition = new Vector2D(GO_position.x + ENEMYSHIP_RADIUS, GO_position.y + ENEMYSHIP_RADIUS);
            bulletPosition.addScaled(shootDirection, GO_radius * 2 * multiplier);
            Vector2D bulletVelocity = new Vector2D(GO_velocity);
            bulletVelocity.addScaled(shootDirection, MUZZLE_VELOCITY);
            if(i == 1){
                bulletVelocity.rotate(0.25);
            }
            else if(i == 2){
                bulletVelocity.rotate(-0.25);
            }
            bullet.add(new Bullet(game, bulletPosition, bulletVelocity.mult(multiplier), true));
        }
        SoundManager.fire();
    }
}
