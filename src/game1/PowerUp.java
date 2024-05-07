package game1;

import utilities.Vector2D;

import java.awt.*;

import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;

public class PowerUp extends GameObject{
    public static final int POWERUP_RADIUS = 16;
    public PowerUpType type;
    public long activateTime;
    public int lifetime = 5000;

    public PowerUp(Game game, Vector2D position) {
        super(game, position, new Vector2D(0,0), POWERUP_RADIUS);
        type = PowerUpType.values()[(int) Math.floor((Math.random() * PowerUpType.values().length))];
        GO_NAME = "POWERUP";
    }

    // creates powerup randomly on top or bottom of screen
    public static PowerUp makePowerUp(Game game){
        if(Math.random() * 2 < 1){ // spawn powerup on top
            return new PowerUp(game, new Vector2D((Math.random() * (FRAME_WIDTH / 2)) + (FRAME_WIDTH / 2) - 50, 50 - POWERUP_RADIUS));
        }
        else{ // spawn powerup on bottom
            return new PowerUp(game, new Vector2D((Math.random() * (FRAME_WIDTH / 2)) + (FRAME_WIDTH / 2) - 50, FRAME_HEIGHT - 50));
        }
    }

    @Override
    public void hit() {
        super.hit();
        this.activateTime = System.currentTimeMillis();
        game.activatePowerUp(this);
    }

    @Override
    public void draw(Graphics2D g) {
        if(type == PowerUpType.MINER){
            g.setColor(Color.GREEN);
        }
        else if(type == PowerUpType.INCSCORE){
            g.setColor(Color.BLUE);
        }
        else if(type == PowerUpType.GUN){
            g.setColor(Color.RED);
        }
        g.fillOval((int) GO_position.x, (int) GO_position.y, POWERUP_RADIUS * 2, POWERUP_RADIUS * 2);
    }
}
