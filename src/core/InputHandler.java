package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    // Các biến trạng thái cho các phím kỹ năng
    private boolean qPressed = false;
    private boolean wPressed = false;
    private boolean ePressed = false;
    private boolean rPressed = false;

    // Biến trạng thái "đã tiêu thụ" để ngăn việc kích hoạt liên tục
    private boolean qConsumed = false;
    private boolean wConsumed = false;
    private boolean eConsumed = false;
    private boolean rConsumed = false;

    @Override
    public void keyTyped(KeyEvent e) {
        // Không sử dụng
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

    // Các phương thức getter đã được điều chỉnh để tự động reset trạng thái
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
