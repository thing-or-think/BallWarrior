package ui.element;

import ui.base.AnchorType;
import ui.base.TextElement;
import java.awt.*;

public class Label extends TextElement {

    public Label(String text, int x, int y, Font font, Color color) {
        super(text, x, y, font, color);
    }

    public Label(String text, int x, int y, Font font, AnchorType anchorType) {
        super(text, x, y, font, anchorType);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(font);
        g.setColor(color);

        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int drawX = x;
        int drawY = y + fm.getAscent(); // Đưa y từ top -> baseline
        switch (anchorType) {
            case TOP_LEFT:
                drawX = x;
                drawY = y + fm.getAscent();
                break;

            case CENTER_BASELINE:
                drawX = x - textWidth / 2;
                drawY = y;
                break;

            case CENTER_TOP:
                drawX = x - textWidth / 2;
                drawY = y + fm.getAscent();
                break;

            case CENTER_MIDDLE:
                drawX = x - textWidth / 2;
                drawY = y + (fm.getAscent() - fm.getDescent()) / 2;
                break;

            case BASELINE_LEFT:
                drawX = x;
                drawY = y;
                break;
        }
        g.drawString(text, drawX, drawY);
    }
}
