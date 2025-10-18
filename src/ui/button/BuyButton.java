package ui.button;

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

    @Override
    public void draw(Graphics2D g) {
        // Màu nền (hover sáng hơn)
        g.setColor(hovered ? new Color(255, 230, 50) : new Color(255, 210, 0));
        g.fillRoundRect(bound.x, bound.y, bound.width, bound.height, 25, 25);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(bound.x, bound.y, bound.width, bound.height, 25, 25);

        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = bound.x + (bound.width - textWidth) / 2;
        int textY = bound.y + (bound.height + fm.getAscent()) / 2 - 3;
        g.drawString(text, textX, textY);
    }
    @Override
    public void onClick() {
        if (activity != null)
            activity.run();
    }
}
