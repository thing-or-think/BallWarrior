package ui.button;

import ui.base.Button;
import java.awt.*;

public class LeftArrowButton extends Button {

    public LeftArrowButton(String text, int x, int y, FontMetrics fm, Font font) {
        super(text, x, y, fm, font);
    }

    public LeftArrowButton(int x, int y, int width, int height) {
        super(null, x, y, width, height, null);
    }

    @Override
    public void draw(Graphics2D g) {
        // Nền nút
        g.setColor(hovered ? Color.GRAY : Color.DARK_GRAY);
        g.fill(bound);

        // Viền
        g.setColor(Color.WHITE);
        g.draw(bound);

        // ==== Mũi tên trái ====
        int arrowSize = bound.height / 2; // kích thước tam giác
        int padding = bound.height / 4;   // khoảng cách từ viền trái
        int centerY = bound.y + bound.height / 2;

        int[] xs = {
                bound.x + padding,                    // đỉnh trái (nhọn)
                bound.x + padding + arrowSize,         // góc phải trên
                bound.x + padding + arrowSize          // góc phải dưới
        };
        int[] ys = {
                centerY,                              // giữa (mũi)
                centerY - arrowSize / 2,              // trên
                centerY + arrowSize / 2               // dưới
        };

        g.setColor(Color.WHITE);
        g.fillPolygon(xs, ys, 3);

        // ==== Văn bản (nếu có) ====
        if (text != null && !text.isEmpty()) {
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = bound.x + bound.width - textWidth - padding; // canh phải sau mũi tên
            int textY = bound.y + (bound.height + fm.getAscent()) / 2 - 2;
            g.drawString(text, textX, textY);
        }
    }

    @Override
    public void onClick() {
        performAction();
    }
}
