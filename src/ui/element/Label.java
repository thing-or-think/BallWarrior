package ui.element;

import ui.base.TextElement;
import java.awt.*;

public class Label extends TextElement {

    public Label(String text, int x, int y, Font font, Color color) {
        super(text, x, y, font, color);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(font);
        g.setColor(color);

        FontMetrics fm = g.getFontMetrics();

        // Tính toạ độ vẽ (Graphics2D.drawString dùng baseline)
        int drawX = x;
        int drawY = y + fm.getAscent(); // Đưa y từ top -> baseline

        g.drawString(text, drawX, drawY);
    }
}
