package ui.button;

import utils.Constants;
import ui.base.Button;

import java.awt.*;

public class MenuButton extends Button {

    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color HOVER_COLOR = Color.YELLOW;

    public MenuButton(String text, int x, int y, FontMetrics fm) {
        super(text, x, y, fm, new Font("Serif", Font.PLAIN, 32));
        bound = new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public MenuButton(String text, int x, int y, FontMetrics fm, Runnable activity) {
        this(text, x, y, fm);
        setActivity(activity);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(font);

        if (hovered) {
            Font hoverFont = font.deriveFont(Font.PLAIN, 40f);
            g.setFont(hoverFont);
            g.setColor(HOVER_COLOR);
            String decoratedText = "> " + text + " <";

            int textWidth = g.getFontMetrics().stringWidth(decoratedText);
            int drawX = (Constants.WINDOW_WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(decoratedText, drawX, drawY);
        } else {
            g.setColor(DEFAULT_COLOR);

            int textWidth = g.getFontMetrics().stringWidth(text);
            int drawX = (Constants.WINDOW_WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(text, drawX, drawY);
        }
    }

    @Override
    public void onClick() {
        System.out.println("Button clicked: " + text);
        if (activity != null) {
            activity.run();
        }
    }
}
