package game1;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keys extends KeyAdapter implements Controller { // takes key presses and put them into action class to store
    Action action;
    public Keys() {
        action = new Action();
    }

    public Action action() {
        // this is defined to comply with the standard interface
        return action;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP -> action.thrust = 1;
            case KeyEvent.VK_LEFT -> action.turn = -1;
            case KeyEvent.VK_RIGHT -> action.turn = +1;
            case KeyEvent.VK_SPACE -> action.shoot = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP -> action.thrust = 0;
            case KeyEvent.VK_LEFT -> action.turn = 0;
            case KeyEvent.VK_RIGHT -> action.turn = 0;
            case KeyEvent.VK_SPACE -> action.shoot = false;
        }
    }
}