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
    private boolean mouseClickedOnce = false;
    private int scrollAmount = 0;

    private long lastClickTime = 0;
    private static final long CLICK_COOLDOWN = 200; // 200 mili giây

    private final boolean[] keys = new boolean[256];
    private final boolean[] prevKeys = new boolean[256];

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key >= 0 && key < keys.length) keys[key] = true;

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

    public boolean consumeClick() {
        if (mouseClickedOnce) {
            long currentTime = System.currentTimeMillis();
            // Kiểm tra xem đã đủ thời gian trôi qua kể từ lần click cuối chưa
            if (currentTime - lastClickTime > CLICK_COOLDOWN) {
                lastClickTime = currentTime; // Cập nhật thời gian click cuối
                mouseClickedOnce = false;      // Tiêu thụ sự kiện click này
                return true;                   // Trả về true: click hợp lệ
            }
            // Nếu click quá nhanh, vẫn tiêu thụ sự kiện nhưng không coi là hợp lệ
            mouseClickedOnce = false;
        }
        return false; // Click không hợp lệ (do quá nhanh hoặc không có sự kiện)
    }


}
