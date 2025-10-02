package ui;

import game.ScoreSystem;
import utils.Constants;

import java.awt.*;

public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);

    /**
     * Vẽ HUD hiển thị thông tin người chơi:
     * - Điểm (Score) bên trái
     * - Mạng (Lives) bên phải
     */
    public static void render(Graphics g, ScoreSystem scoreSystem) {
        g.setColor(Color.WHITE);
        g.setFont(FONT);

        g.drawString("Score: " + scoreSystem.getScore(), 20, 30);

        String livesText = "Lives: " + scoreSystem.getLives();
        int textWidth = g.getFontMetrics().stringWidth(livesText);
        g.drawString(livesText, Constants.WIDTH - textWidth - 20, 30);
    }
}
