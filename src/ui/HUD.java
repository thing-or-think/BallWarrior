package ui;

import game.ScoreSystem;
import utils.Constants;

import java.awt.*;

public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);

    // Dữ liệu và trạng thái hiệu ứng combo
    private float lastKnownCombo = 1f;
    private long comboAnimStart = 0;

    /**
     * Vẽ HUD hiển thị thông tin người chơi:
     * - Điểm (Score) bên trái
     * - Mạng (Lives) bên phải
     * - Combo (combo) giữa cạnh phải
     */
    public void render(Graphics g, ScoreSystem scoreSystem) {

        //cập nhật hiệu ứng
        float currentCombo = scoreSystem.getCombo();
        if (currentCombo > lastKnownCombo) {
            comboAnimStart = System.currentTimeMillis();
        }
        lastKnownCombo = currentCombo;

        g.setColor(Color.WHITE);
        g.setFont(FONT);

        //vẽ score và lives
        g.drawString("Score: " + scoreSystem.getScore(), 20, 30);

        String livesText = "Lives: " + scoreSystem.getLives();
        int textWidth = g.getFontMetrics().stringWidth(livesText);
        g.drawString(livesText, Constants.WIDTH - textWidth - 20, 30);

        // vẽ combo chỉ khi > 1
        if (currentCombo > 1f) {
            long elapsed = System.currentTimeMillis() - comboAnimStart;
            double progress = elapsed / 300.0; // hiệu ứng phóng to/thu nhỏ
            float scale = (float)(1.5 - 0.5 * Math.min(progress, 1.0));

            Graphics2D g2d = (Graphics2D) g.create();

            // chọn màu dựa theo giá trị combo
            Color comboColor;
            if (currentCombo < 2f) {
                comboColor = Color.YELLOW;
            } else if (currentCombo < 3f) {
                comboColor = Color.ORANGE;
            } else {
                // combo max → hiệu ứng nhấp nháy gradient đỏ <-> vàng
                float t = (float)(Math.sin(System.currentTimeMillis() / 200.0) * 0.5 + 0.5);
                int r = (int)(255 * (1 - t) + 255 * t); // đỏ luôn 255
                int gVal = (int)(0 * (1 - t) + 255 * t); // xanh dao động 0 -> 255
                int b = 0;
                comboColor = new Color(r, gVal, b);
            }
            g2d.setColor(comboColor);

            String comboText = "x" + currentCombo;
            Font font = g.getFont().deriveFont(35f * scale); // chữ to hơn khi nhấp nháy
            g2d.setFont(font);

            // vẽ combo ở bên phải, giữa màn hình
            textWidth = g2d.getFontMetrics().stringWidth(comboText);
            int x = Constants.WIDTH- textWidth - 50;
            int y = Constants.HEIGHT / 2;
            g2d.drawString(comboText, x, y);

            g2d.dispose();
        }
    }
}
