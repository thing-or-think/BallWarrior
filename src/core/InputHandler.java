package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler implements KeyListener {

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    private boolean qPressed = false;
    private boolean wPressed = false;
    private boolean ePressed = false;
    private boolean rPressed = false;

    private boolean qConsumed = false;
    private boolean wConsumed = false;
    private boolean eConsumed = false;
    private boolean rConsumed = false;

    private int mouseX = 0;
    private int mouseY = 0;

    private boolean mousePressed = false;
    private boolean mouseClickedOnce = false;
    private int scrollAmount = 0;

    private final boolean[] keys = new boolean[256];
    private final boolean[] prevKeys = new boolean[256];

    private MouseAdapter mouseAdapter;

    public InputHandler() {
        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
                mouseClickedOnce = true;
            }
        };
    }

    public MouseAdapter createMouseAdapter() {
        return this.mouseAdapter;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean consumeClick() {
        if (mouseClickedOnce) {
            mouseClickedOnce = false;
            return true;
        }
        return false;
    }

    public void resetMouse() {
        mousePressed = false;
        mouseClickedOnce = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key >= 0 && key < keys.length) {
            keys[key] = true;
        }

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (key == KeyEvent.VK_SPACE) {
            spacePressed = true;
        } else if (key == KeyEvent.VK_Q) {
            if (!qConsumed) {
                qPressed = true;
                qConsumed = true;
            }
        } else if (key == KeyEvent.VK_W) {
            if (!wConsumed) {
                wPressed = true;
                wConsumed = true;
            }
        } else if (key == KeyEvent.VK_E) {
            if (!eConsumed) {
                ePressed = true;
                eConsumed = true;
            }
        } else if (key == KeyEvent.VK_R) {
            if (!rConsumed) {
                rPressed = true;
                rConsumed = true;
            }
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
        } else if (key == KeyEvent.VK_SPACE) {
            spacePressed = false;
        } else if (key == KeyEvent.VK_Q) {
            qPressed = false;
            qConsumed = false;
        } else if (key == KeyEvent.VK_W) {
            wPressed = false;
            wConsumed = false;
        } else if (key == KeyEvent.VK_E) {
            ePressed = false;
            eConsumed = false;
        } else if (key == KeyEvent.VK_R) {
            rPressed = false;
            rConsumed = false;
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isSpacePressed() {
        return spacePressed;
    }

    public boolean isQPressed() {
        if (qPressed) {
            qPressed = false;
            return true;
        }
        return false;
    }

    public boolean isWPressed() {
        if (wPressed) {
            wPressed = false;
            return true;
        }
        return false;
    }

    public boolean isEPressed() {
        if (ePressed) {
            ePressed = false;
            return true;
        }
        return false;
    }

    public boolean isRPressed() {
        if (rPressed) {
            rPressed = false;
            return true;
        }
        return false;
    }

    public void update() {
        System.arraycopy(keys, 0, prevKeys, 0, keys.length);
    }
}
