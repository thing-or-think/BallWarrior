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
        FontMetrics fm;
        int textWidth;
        int drawX;
        int drawY;

        if (hovered) {
            // 1. Chỉ đổi font và màu, KHÔNG đổi text
            Font hoverFont = font.deriveFont(Font.PLAIN, 40f);
            g.setFont(hoverFont);
            g.setColor(HOVER_COLOR);

            // 2. Dùng text y hệt như đã được truyền vào (vd: ">> Player 1 <<")
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(text);
            drawX = (Constants.WINDOW_WIDTH - textWidth) / 2;
            drawY = bound.y + fm.getAscent() + (bound.height - fm.getHeight()) / 2; // Căn giữa Y tốt hơn

            g.drawString(text, drawX, drawY);
        } else {
            // 3. Vẽ trạng thái bình thường (không hover)
            g.setFont(font);
            g.setColor(DEFAULT_COLOR);

            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(text);
            drawX = (Constants.WINDOW_WIDTH - textWidth) / 2;
            drawY = bound.y + fm.getAscent() + (bound.height - fm.getHeight()) / 2; // Căn giữa Y tốt hơn

            g.drawString(text, drawX, drawY);
        }

    }

    @Override
    public void onClick() {
        System.out.println("Button clicked: " + text);
        performAction();
    }
}
