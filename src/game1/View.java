package game1;

import utilities.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;

public class View extends JComponent {
    // background colour
    public static final Color BG_COLOR = Color.black;

    private Image im = Constants.MILKYWAY1;
    private AffineTransform bgTransf;
    private Game game;

    // constructor of view, initialises transform for background image
    public View(Game game) {
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = (imWidth > Constants.FRAME_WIDTH? 1 :
                Constants.FRAME_WIDTH/imWidth);
        double stretchy = (imHeight > FRAME_HEIGHT? 1 :
                FRAME_HEIGHT/imHeight);
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        // if the game ends, dispaly score/highscores
        if(game.gameEnded){
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.drawString("High Scores: ", 100, 65);
            if(!game.highScoresToDisplay.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    g.drawString(game.highScoresToDisplay.get(i), 125, 105 + (50 * i));
                }
            }
            g.drawString("Your score: " + game.getScore(), 100, 405);
            g.drawString("See the console for saving your score, or seeing highscores.", 100, 455);
        }
        else { // otherwise draw all objects/particles + text for lives remaining, current score and current powerups time remaining
            g.drawImage(im, bgTransf,null);

            synchronized(Game.class) {
                for (GameObject object : game.objects) {
                    object.draw(g);
                }
                for(Particle p : game.particles){
                    p.draw(g);
                }
            }

            g.setColor(Color.WHITE);

            // lives text
            g.drawString("Lives: " + game.lives, FRAME_WIDTH - 50, 15);

            // score text
            g.drawString(String.valueOf(game.getScore()), 10, 15);

            // powerup text
            for(int i = 0; i < game.activePowerUps.size(); i++){
                PowerUp p = game.activePowerUps.get(i);
                if(p.type == PowerUpType.MINER){
                    g.drawString("MINER: " + (int) ((p.lifetime - (System.currentTimeMillis() - p.activateTime)) / 1000), 10, FRAME_HEIGHT - (20 * (i + 1)));
                }
                else if(p.type == PowerUpType.GUN){
                    g.drawString("GUN: " + (int) ((p.lifetime - (System.currentTimeMillis() - p.activateTime)) / 1000), 10, FRAME_HEIGHT - (20 * (i + 1)));
                }
            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }

}