package ui.button;

import utils.Constants;
import ui.base.Button;

import java.awt.*;

public class TextButton extends Button {

    public TextButton(String text, int x, int y, FontMetrics fm) {
        super(text, x, y, fm);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(baseFont);

        if (hovered) {
            Font hoverFont = baseFont.deriveFont(Font.PLAIN, 40f);
            g.setFont(hoverFont);
            g.setColor(Color.YELLOW);
            String decoratedText = "> " + text + " <";

            int textWidth = g.getFontMetrics().stringWidth(decoratedText);
            int drawX = (Constants.WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(decoratedText, drawX, drawY);
        } else {
            g.setColor(Color.WHITE);

            int textWidth = g.getFontMetrics().stringWidth(text);
            int drawX = (Constants.WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(text, drawX, drawY);
        }
    }

    @Override
    public void onClick() {
        // Hành động khi nút được bấm (sẽ được override ở lớp khác nếu cần)
        System.out.println("Button clicked: " + text);
    }
}
