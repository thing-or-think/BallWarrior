package ui.element;

import ui.base.TextElement;
import utils.Constants;

import java.awt.*;

public class ComboLabel extends TextElement {
    private float comboValue = 1f;
    private long animStart = 0;

    public ComboLabel(int x, int y, Font font) {
        super("", x, y, font, Color.YELLOW);
    }

    public void setComboValue(float comboValue) {
        if (comboValue > this.comboValue) {
            animStart = System.currentTimeMillis();
        }
        this.comboValue = comboValue;
        this.text = "x" + comboValue;
    }

    @Override
    public void draw(Graphics2D g) {
        if (comboValue - 1f <= Constants.COLLISION_EPSILON) {
            return;
        }

        long elapsed = System.currentTimeMillis() - animStart;
        double progress = elapsed / 300.0;
        float scale = (float) (1.5 - 0.5 * Math.min(progress, 1.0));

        // màu tùy combo
        if (comboValue < 2f) {
            color = Color.YELLOW;
        } else if (comboValue < 3f) {
            color = Color.ORANGE;
        } else {
            float t = (float) (Math.sin(System.currentTimeMillis() / 200.0) * 0.5 + 0.5);
            int r = 255;
            int gVal = (int) (255 * t);
            color = new Color(r, gVal, 0);
        }

        g.setColor(color);
        Font scaledFont = font.deriveFont(35f * scale);
        g.setFont(scaledFont);

        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x - textWidth, y);
    }
}
