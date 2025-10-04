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

    private int mouseX, mouseY;
    private boolean mousePressed;
    private boolean mouseClicked;

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
                mouseClicked = true;
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
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }

    public void resetMouse() {
        mousePressed = false;
        mouseClicked = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = true;
        } else if (keyCode == KeyEvent.VK_Q) {
            if (!qConsumed) {
                qPressed = true;
                qConsumed = true;
            }
        } else if (keyCode == KeyEvent.VK_W) {
            if (!wConsumed) {
                wPressed = true;
                wConsumed = true;
            }
        } else if (keyCode == KeyEvent.VK_E) {
            if (!eConsumed) {
                ePressed = true;
                eConsumed = true;
            }
        } else if (keyCode == KeyEvent.VK_R) {
            if (!rConsumed) {
                rPressed = true;
                rConsumed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = false;
        } else if (keyCode == KeyEvent.VK_Q) {
            qPressed = false;
            qConsumed = false;
        } else if (keyCode == KeyEvent.VK_W) {
            wPressed = false;
            wConsumed = false;
        } else if (keyCode == KeyEvent.VK_E) {
            ePressed = false;
            eConsumed = false;
        } else if (keyCode == KeyEvent.VK_R) {
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
}
