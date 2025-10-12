package ui.base;

import java.awt.*;

public abstract class Button extends TextElement {

    protected int x, y;                // Góc trên-trái của nút
    protected int width, height;       // Kích thước nút
    protected Rectangle bound;         // Vùng va chạm / click
    protected boolean hovered;         // Trạng thái hover
    protected Runnable activity;       // Hành động khi click

    // Constructor 1: tự đo kích thước theo text
    public Button(String text, int x, int y, FontMetrics fm, Font font) {
        super(text, x, y, font);

        this.x = x;
        this.y = y;
        this.width = fm.stringWidth(text);
        this.height = fm.getAscent();

        this.bound = new Rectangle(x, y, width, height);
    }

    // Constructor 2: dùng kích thước cố định
    public Button(String text, int x, int y, int width, int height, Font font) {
        super(text, x, y, font);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.bound = new Rectangle(x, y, width, height);
    }

    /** Vẽ nút — mỗi subclass (PlayButton, LeftArrowButton, ...) sẽ override */
    public abstract void draw(Graphics2D g);

    /** Gọi khi click — subclass có thể override hoặc dùng setActivity() */
    public abstract void onClick();

    /** Kiểm tra chuột có nằm trong nút không */
    public boolean contains(int mx, int my) {
        return bound.contains(mx, my);
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setActivity(Runnable activity) {
        this.activity = activity;
    }

    public void performAction() {
        if (activity != null) activity.run();
    }

    /** Getter tiện dụng nếu cần */
    public Rectangle getBounds() {
        return bound;
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }
}
