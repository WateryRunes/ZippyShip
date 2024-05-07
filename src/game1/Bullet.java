package game1;

import utilities.Vector2D;

import java.awt.*;

import static game1.Constants.*;

public class Bullet extends GameObject {
    public static final int RADIUS = 2;
    public static final double PLAYER_BULLET_LIFE = 2;
    public static final double ENEMY_BULLET_LIFE = 4;
    public double lifetime;

    public Bullet(Game game, Vector2D position, Vector2D velocity, boolean player) {
        super(game, position, velocity, RADIUS);
        lifetime = player ? PLAYER_BULLET_LIFE : ENEMY_BULLET_LIFE; // different lifetime depending on whether shot by player or not
        GO_NAME = "BULLET";
    }

    public void update() {
        super.update();
        GO_position.verticalWrap(FRAME_WIDTH, FRAME_HEIGHT);
        lifetime -= DT;
        if (lifetime <= 0) {
            GO_dead = true;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) (GO_position.x - GO_radius), (int) (GO_position.y - GO_radius), (int) (2 * GO_radius), (int) (2 * GO_radius));
    }
}
