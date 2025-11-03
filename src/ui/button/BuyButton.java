package ui.button;

import ui.base.AnchorType;
import ui.base.Button;

import java.awt.*;

public class BuyButton extends Button {
    public BuyButton(String text, int x, int y, int width, int height, Font font, Runnable activity) {
        super(text, x, y, width, height, font);
        setActivity(activity);
    }

    public BuyButton(String text, int x, int y, int width, int height, Font font) {
        super(text, x, y, width, height, font);
    }

    public BuyButton(String text, int x, int y, Font font, AnchorType anchorType) {
        super(text, x, y, font, anchorType);
    }

    @Override
    public void draw(Graphics2D g) {
        // --- Vẽ nền và viền ---
        g.setColor(hovered ? new Color(255, 230, 50) : new Color(255, 210, 0));
        g.fillRoundRect(bound.x, bound.y, bound.width, bound.height, 25, 25);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(bound.x, bound.y, bound.width, bound.height, 25, 25);

        // --- Vẽ chữ nằm giữa khung ---
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int textX = bound.x + (bound.width - textWidth) / 2;
        int textY = bound.y + (bound.height + textHeight) / 2 - 3;

        g.setColor(Color.DARK_GRAY);
        g.drawString(text, textX, textY);
    }

    @Override
    public void onClick() {
        performAction();
    }
}
