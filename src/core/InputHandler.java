package core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    // Skill keys
    private boolean qPressed = false;
    private boolean wPressed = false;
    private boolean ePressed = false;
    private boolean rPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT: leftPressed = true; break;
            case KeyEvent.VK_RIGHT: rightPressed = true; break;
            case KeyEvent.VK_SPACE: spacePressed = true; break;
            case KeyEvent.VK_Q: qPressed = true; break;
            case KeyEvent.VK_W: wPressed = true; break;
            case KeyEvent.VK_E: ePressed = true; break;
            case KeyEvent.VK_R: rPressed = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT: leftPressed = false; break;
            case KeyEvent.VK_RIGHT: rightPressed = false; break;
            case KeyEvent.VK_SPACE: spacePressed = false; break;
            case KeyEvent.VK_Q: qPressed = false; break;
            case KeyEvent.VK_W: wPressed = false; break;
            case KeyEvent.VK_E: ePressed = false; break;
            case KeyEvent.VK_R: rPressed = false; break;
        }
    }

    // Getters
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isSpacePressed() { return spacePressed; }

    public boolean isQPressed() { return qPressed; }
    public boolean isWPressed() { return wPressed; }
    public boolean isEPressed() { return ePressed; }
    public boolean isRPressed() { return rPressed; }
}
