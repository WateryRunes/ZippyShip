package game1;

import utilities.ImageManager;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Constants { // stores some constants used thorughout the program
    public static final int FRAME_HEIGHT = 480;
    public static final int FRAME_WIDTH = 640;
    public static final Dimension FRAME_SIZE = new Dimension(
            Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
    // sleep time between two frames
    public static final int DELAY = 20;  // in milliseconds
    public static final double DT = DELAY / 1000.0;  // in seconds
    public static final double MAX_SPEED = 150.0; // in pixels per second
    public static final Random RANDOM = new Random();

    public static Image ASTEROID1, MILKYWAY1;
    static {
        try {
            ASTEROID1 = ImageManager.loadImage("asteroid1");
            MILKYWAY1 = ImageManager.loadImage("milkyway1");
        } catch (IOException e) { e.printStackTrace(); }
    }
}