package ui.button;

import ui.base.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * LevelButton
 * -------------------------------------------------------------------
 * Nút chọn màn chơi, hiển thị tên + độ khó + ảnh preview.
 * -------------------------------------------------------------------
 */
public class LevelButton extends Button {

    private final String difficulty;
    private final String previewPath;
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color HOVER_COLOR = new Color(255, 215, 0);
    private static final int PREVIEW_WIDTH = 120;
    private static final int PREVIEW_HEIGHT = 90;
    private static final int PREVIEW_MARGIN_X = 700;

    private BufferedImage previewImage;

    public LevelButton(String text, String difficulty, String previewPath, int x, int y, FontMetrics fm) {
        super(text, x, y, fm, new Font("Serif", Font.PLAIN, 32));
        this.difficulty = difficulty;
        this.previewPath = previewPath;
        loadPreview();
    }

    /** Tải ảnh preview một lần duy nhất khi khởi tạo */
    private void loadPreview() {
        if (previewPath != null) {
            try {
                previewImage = ImageIO.read(new File("assets/previews/" + previewPath));
            } catch (IOException e) {
                System.err.println("Không thể tải preview cho level: " + text);
                previewImage = null;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Vẽ nền nút
        if (hovered) {
            g.setColor(HOVER_COLOR);
            g.fillRoundRect(bound.x - 15, bound.y - 15, bound.width + 30, bound.height + 30, 20, 20);
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(bound.x - 10, bound.y - 10, bound.width + 20, bound.height + 20, 15, 15);
        }

        // Vẽ text
        g.setFont(font);
        g.setColor(DEFAULT_COLOR);
        String label = text + " - " + difficulty;
        g.drawString(label, bound.x, bound.y + bound.height);

        // Vẽ preview
        if (previewImage != null) {
            g.drawImage(previewImage, PREVIEW_MARGIN_X, bound.y - 10, PREVIEW_WIDTH, PREVIEW_HEIGHT, null);
        } else {
            // Nếu không có ảnh, vẽ khung màu giả lập
            Color[] demoColors = {
                    new Color(173, 216, 230),
                    new Color(152, 251, 152),
                    new Color(255, 182, 193),
                    new Color(240, 230, 140),
                    new Color(221, 160, 221)
            };
            g.setColor(demoColors[Math.abs(text.hashCode()) % demoColors.length]);
            g.fillRoundRect(PREVIEW_MARGIN_X, bound.y - 10, PREVIEW_WIDTH, PREVIEW_HEIGHT, 15, 15);

            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(PREVIEW_MARGIN_X, bound.y - 10, PREVIEW_WIDTH, PREVIEW_HEIGHT, 15, 15);

            g.setFont(font.deriveFont(Font.PLAIN, 14f));
            g.drawString("Preview", PREVIEW_MARGIN_X + 25, bound.y + 40);
        }
    }

    @Override
    public void onClick() {
        System.out.println("Level selected: " + text + " (" + difficulty + ")");
        performAction();
    }
}
