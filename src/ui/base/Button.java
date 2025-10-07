package ui.base;

import java.awt.*;

public abstract class Button {

    protected String text;
    protected Rectangle bound;
    protected boolean hovered;
    protected Font baseFont;

    public Button(String text, int x, int y, FontMetrics fm) {
        this.text = text;

        int width = fm.stringWidth(text);
        int height = fm.getAscent();
        int Rx = x - width / 2;  // x là tâm giữa nút
        int Ry = y;

        this.bound = new Rectangle(Rx, Ry - height, width, height);
        this.baseFont = new Font("Serif", Font.PLAIN, 32);
    }

    public abstract void draw(Graphics2D g);

    public abstract void onClick();

    public boolean contains(int mx, int my) {
        return bound.contains(mx, my);
    }

    public int getY() {
        return bound.y;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public String getText() {
        return text;
    }
}
