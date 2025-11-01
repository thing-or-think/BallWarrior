package ui.button;

import ui.base.Button;
import java.awt.*;

public class PlayButton extends Button {

    public PlayButton(String text, int x, int y, FontMetrics fm, Font font) {
        super(text, x, y, fm, font);
    }

    public PlayButton(String text, int x, int y, int width, int height, Font font, Runnable activity) {
        super(text, x, y, width, height, font);
        setActivity(activity);
    }


    @Override
    public void draw(Graphics2D g) {
        // Màu nền (hover sáng hơn)
        g.setColor(hovered ? new Color(80, 140, 255) : new Color(60, 120, 250));
        g.fillRoundRect(bound.x, bound.y, bound.width, bound.height, 12, 12);

        // Viền trắng
        g.setColor(Color.WHITE);
        g.drawRoundRect(bound.x, bound.y, bound.width, bound.height, 12, 12);

        // Chữ “PLAY”
        g.setFont(font);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = bound.x + (bound.width - textWidth) / 2;
        int textY = bound.y + (bound.height + fm.getAscent()) / 2 - 3;
        g.drawString(text, textX, textY);
    }

    @Override
    public void onClick() {
        performAction();
    }
}
