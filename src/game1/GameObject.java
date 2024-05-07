package game1;

import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game1.Constants.*;

public abstract class GameObject {
    public Game game;
    public Vector2D GO_position;
    public Vector2D GO_velocity;
    public boolean GO_dead;
    public int GO_radius;
    public String GO_NAME;
    public Color GO_explodeColor = null;

    public GameObject(Game game, Vector2D position, Vector2D velocity, int radius){
        this.GO_position = position;
        this.GO_velocity = velocity;
        this.GO_radius = radius;
        this.game = game;
        GO_dead = false;
    }

    public void hit(){
        GO_dead = true;
    }

    public void update() {
        GO_position.addScaled(GO_velocity, DT);
    }

    public abstract void draw(Graphics2D g); // draw different for all children

    public boolean overlap(GameObject other){ // pythagoras
        return Math.sqrt((GO_position.x - other.GO_position.x) * (GO_position.x - other.GO_position.x) + (GO_position.y - other.GO_position.y) * (GO_position.y - other.GO_position.y)) < GO_radius + other.GO_radius;
    }

    public void collisionHandling(GameObject other) { // only allows destruction if canDestroy dictionary says that object can destroy the other
        if (this.getClass() != other.getClass()) {
            if(this.overlap(other)){
                if (game.canDestroy.get(other.GO_NAME).contains(this.GO_NAME)) {
                    this.hit();
                }
                if (game.canDestroy.get(this.GO_NAME).contains(other.GO_NAME)) {
                    other.hit();
                }
            }
        }
    }
}
