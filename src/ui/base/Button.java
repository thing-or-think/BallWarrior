package ui.base;

import utils.TextUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Button extends TextElement {

    protected int width, height;       // Kích thước nút
    protected Rectangle bound;         // Vùng va chạm / click
    protected boolean hovered;         // Trạng thái hover
    protected boolean clicked;         // Trạng tháy clicked
    protected Runnable activity;       // Hành động khi click
    protected BufferedImage icon;      // Icon

    protected ButtonGroup buttonGroup;

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
    // Constructor 3: dùng icon
    public Button(String text, BufferedImage icon, int x, int y, int width, int height) {
        super(text, x, y, new Font("Serif", Font.PLAIN, 32));
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.bound = new Rectangle(x, y, width, height);
    }

    public Button(String text, int x, int y, Font font, AnchorType anchorType) {
        super(text, x, y, font, anchorType);
        Dimension dimension = TextUtils.measureText(text, font);
        this.width = dimension.width + 20;
        this.height = dimension.height + 20;
        this.bound = new Rectangle(x, y, width, height);
        setAnchorType(anchorType);
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

    public void setText(String text) {
        this.text = text;
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

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setBound(Rectangle bound) {
        this.bound = bound;
        this.x = bound.x;
        this.y = bound.y;
        this.width = bound.width;
        this.height = bound.height;
    }

    public void setButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    public void setAnchorType(AnchorType anchorType) {
        super.setAnchorType(anchorType);
        switch (anchorType) {
            case TOP_LEFT -> {
                // (x, y) là góc trên trái
            }
            case CENTER_TOP -> {
                bound.x = bound.x - bound.width / 2;
            }
            case CENTER_MIDDLE -> {
                bound.x = bound.x - bound.width / 2;
                bound.y = bound.y - bound.height / 2;
            }
            case CENTER_BASELINE -> {
                bound.x = bound.x - bound.width / 2;
                bound.y = bound.y - bound.height;
            }
            case BASELINE_LEFT -> {
                bound.y = bound.y - bound.height;
            }
        }
    }
}
