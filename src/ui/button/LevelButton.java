package ui.button;

import ui.base.Button;

import javax.swing.*;
import java.awt.*;

public class LevelButton extends Button {

    private final String difficulty;
    private final String preview;

    public LevelButton(String text, String difficulty, String preview, int x, int y, FontMetrics fm) {
        super(text, x, y, fm);
        this.difficulty = difficulty;
        this.preview = preview;
    }

    @Override
    public void draw(Graphics2D g) {
        // Vẽ nền (viền chọn)
        if (hovered) {
            g.setColor(new Color(255, 215, 0)); // vàng
            g.fillRoundRect(bound.x - 20, bound.y - 20, bound.width + 40, bound.height + 40, 25, 25);
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        // Vẽ chữ
        g.setFont(baseFont);
        g.drawString(text + " - " + difficulty, bound.x, bound.y + bound.height);

        // Preview ảnh hoặc hình chữ nhật màu
        if (preview != null) {
            Image img = new ImageIcon("assets/previews/" + preview).getImage();
            g.drawImage(img, 700, bound.y - 10, 120, 90, null);
        } else {
            Color[] demoColors = {
                    new Color(173, 216, 230),
                    new Color(152, 251, 152),
                    new Color(255, 182, 193),
                    new Color(240, 230, 140),
                    new Color(221, 160, 221)
            };
            g.setColor(demoColors[Math.abs(text.hashCode()) % demoColors.length]);
            g.fillRoundRect(700, bound.y - 10, 120, 90, 15, 15);

            g.setColor(Color.DARK_GRAY);
            g.drawRect(700, bound.y - 10, 120, 90);
            g.drawString("Preview", 720, bound.y + 40);
        }
    }

    @Override
    public void onClick() {
        if (activity != null) activity.run();
    }
}
