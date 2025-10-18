package ui.base;

import java.awt.*;

public abstract class TextElement {

    protected String text;
    protected int x, y;
    protected Font font;
    protected Color color;
    protected AnchorType anchorType = AnchorType.TOP_LEFT; // giá trị mặc định

    public TextElement(String text, int x, int y, Font font, Color color) {
        this.text = text != null ? text : "";
        this.x = x;
        this.y = y;
        this.font = font != null ? font : new Font("Serif", Font.PLAIN, 24);
        this.color = color != null ? color : Color.WHITE;
    }

    public TextElement(String text, int x, int y, Font font) {
        this.text = text != null ? text : "";
        this.x = x;
        this.y = y;
        this.font = font != null ? font : new Font("Serif", Font.PLAIN, 24);
        this.color = Color.WHITE;
    }

    public TextElement(String text, int x, int y, Font font, AnchorType anchorType) {
        this.text = text != null ? text : "";
        this.x = x;
        this.y = y;
        this.font = font != null ? font : new Font("Serif", Font.PLAIN, 24);
        this.color = Color.WHITE;
        this.anchorType = anchorType;
    }

    public abstract void draw(Graphics2D g);

    public void setText(String text) { this.text = text != null ? text : ""; }
    public void setColor(Color color) { this.color = color; }
    public void setFont(Font font) { this.font = font; }
    public String getText() { return text; }
    public void setAnchorType(AnchorType anchorType) {
        this.anchorType = anchorType;
    }
}
