package game1;

import utilities.Vector2D;
import utilities.SoundManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class Ship extends GameObject {
    public static final int SHIP_RADIUS = 8;
    // rotation velocity in radians per second
    public static final double STEER_RATE = 2* Math.PI;

    // acceleration when thrust is applied
    public static final double MAG_ACC = 300;

    // constant speed loss factor
    public static final double DRAG = 0.01;

    public static final Color COLOR = Color.cyan;

    public static final int MUZZLE_VELOCITY = 100;

    private static final int[] XPSHIP = {10, 0, -10, 0};
    private static final int[] YPSHIP = {10, -10, 10, 0};
    private static final int[] XPTHRUST = {4, 0, -4};
    private static final int[] YPTHRUST = {4, 0, 4};

    private static final int MAX_BULLETS_PER_SECOND = 5;
    public static final double BULLET_INTERVAL = 1000.0 / MAX_BULLETS_PER_SECOND;

    public static final Color explodeColor = Color.CYAN;

    // direction in which the nose of the ship is pointing
    // this will be the direction in which thrust is applied
    // it is a unit vector representing the angle by which the ship has rotated
    public Vector2D direction;

    // controller which provides an Action object in each frame
    private Controller ctrl;

    public Bullet bullet = null;

    public double last_bullet_time = System.currentTimeMillis();

    public boolean canShoot = false;

    public boolean canMine = false;

    public Ship(Controller ctrl, Game game) {
        super(game, new Vector2D(Constants.FRAME_WIDTH / 20, Constants.FRAME_HEIGHT / 2), new Vector2D(), SHIP_RADIUS);
        this.ctrl = ctrl;
        direction = new Vector2D(1, 0); // always look right
        GO_NAME = "SHIP";
        GO_explodeColor = explodeColor;
    }

    @Override
    public void hit(){ // comment out super.hit for invuln (testing purposes)
        super.hit();
        SoundManager.stopThrust();
    }

    public void update() {
        super.update();
        GO_position.verticalWrap(FRAME_WIDTH, FRAME_HEIGHT);
        Action action = ctrl.action();
        direction.rotate(action.turn*STEER_RATE*DT);
        if(GO_velocity.mag() < MAX_SPEED) {
            GO_velocity.addScaled(direction, MAG_ACC * DT * action.thrust);
        }
        GO_velocity.addScaled(GO_velocity, -DRAG);
        if (action.shoot && canShoot) { // will allow shooting only if powerup GUN has been obtained
            double time = System.currentTimeMillis();
            if (time - last_bullet_time >= BULLET_INTERVAL) {
                mkBullet();
                last_bullet_time = time;
                action.shoot = false;
            } else {
                // System.out.println("bullet not fired: " + (time - last_bullet_time));
            }
        }
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(GO_position.x, GO_position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        //g.scale(DRAWING_SCALE, DRAWING_SCALE);
        g.setColor(COLOR);
        g.fillPolygon(XPSHIP, YPSHIP, XPSHIP.length);
        if (ctrl.action().thrust == 1) { // if thrusting
            g.setColor(Color.red);
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
            SoundManager.startThrust();
        }
        else{
            SoundManager.stopThrust();
        }
        g.setTransform(at);
    }

    private void mkBullet() {
        Vector2D bulletPosition = new Vector2D(GO_position);
        bulletPosition.addScaled(direction, GO_radius * 2);
        Vector2D bulletVelocity = new Vector2D(GO_velocity);
        bulletVelocity.addScaled(direction, MUZZLE_VELOCITY);
        bullet = new Bullet(game, bulletPosition, bulletVelocity, true);
        SoundManager.fire();
    }
}
