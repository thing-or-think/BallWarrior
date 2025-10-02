package ui;

import java.awt.*;

public class HUD {

    private int score;
    private int lives;
    private float combo;

    // biến animation cho combo
    private long comboAnimStart = 0;
    private boolean comboVisible = false;

    public HUD(int lives) {
        this.lives = lives;
        this.score = 0;
        this.combo = 1f;
    }

    // cập nhật score
    public void addScore(int value) {
        score += value;
    }

    // cập nhật combo
    public void increaseCombo(float amount) {
        float oldCombo = combo;
        combo = Math.min(combo + amount, 3f);

        if (combo > oldCombo) {
            comboVisible = true;
            comboAnimStart = System.currentTimeMillis();
        }
    }

    // reset combo
    public void resetCombo() {
        combo = 1f;
        comboVisible = false;
    }

    // trừ mạng
    public void loseLife() {
        lives--;
    }

    // getter
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public float getCombo() { return combo; }

    public void render(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, width - 80, 20);

        // vẽ combo chỉ khi > 1
        if (comboVisible && combo > 1f) {
            long elapsed = System.currentTimeMillis() - comboAnimStart;
            double progress = elapsed / 300.0; // hiệu ứng phóng to/thu nhỏ
            float scale = (float)(1.5 - 0.5 * Math.min(progress, 1.0));

            Graphics2D g2d = (Graphics2D) g.create();

            // chọn màu dựa theo giá trị combo
            Color comboColor;
            if (combo < 2f) {
                comboColor = Color.GREEN;
            } else if (combo < 3f) {
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

            String comboText = "x" + combo;
            Font font = g.getFont().deriveFont(35f * scale); // chữ to hơn khi nhấp nháy
            g2d.setFont(font);

            // vẽ combo ở bên phải, giữa màn hình
            int textWidth = g2d.getFontMetrics().stringWidth(comboText);
            int x = width - textWidth - 50;
            int y = height / 2;
            g2d.drawString(comboText, x, y);

            g2d.dispose();
        }
    }


}
