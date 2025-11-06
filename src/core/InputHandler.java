package core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class InputHandler extends KeyAdapter {

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mousePressed = false;
    private boolean prevMousePressed = false;
    private boolean mouseClickedOnce = false;
    private int scrollAmount = 0;

    private final boolean[] keys = new boolean[256];
    private final boolean[] prevKeys = new boolean[256];

    private int lastKeyPressed = -1;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key >= 0 && key < keys.length) {
            keys[key] = true;
            lastKeyPressed = key;
        }

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key >= 0 && key < keys.length) keys[key] = false;

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    public boolean isKeyDown(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public boolean isKeyJustPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode] && !prevKeys[keyCode];
    }

    public boolean isKeyJustReleased(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && !keys[keyCode] && prevKeys[keyCode];
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                mouseClickedOnce = true;  // đánh dấu 1 lần click
                mouseX = e.getX();
                mouseY = e.getY();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseClickedOnce = true; // chỉ đánh dấu khi thả
                mousePressed = false;
                mouseX = e.getX();
                mouseY = e.getY();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollAmount += e.getWheelRotation();
            }
        };
    }

    public void update() {
        System.arraycopy(keys, 0, prevKeys, 0, keys.length);
        prevMousePressed = mousePressed;
        lastKeyPressed = -1;
    }

    public int consumeScroll() {
        int temp = scrollAmount;
        scrollAmount = 0;
        return temp;
    }

    public boolean isMousePressed() { return mousePressed; }
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }


    public void resetMouse() {
        mousePressed = false;
        mouseClickedOnce = false;
    }

    public boolean isMouseJustPressed() {
        return mousePressed && !prevMousePressed;
    }

    public boolean isMouseJustReleased() {
        return !mousePressed && prevMousePressed;
    }

    public boolean consumeClick() {
        if (isMouseJustPressed()) {
            return true;
        }
        return false;
    }

    public int getLastKeyPressed() {
        return lastKeyPressed;
    }

    public void reset() {
        mousePressed = false;
        prevMousePressed = false;

        scrollAmount = 0;
    }
}
