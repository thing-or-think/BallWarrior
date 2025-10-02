package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Constants;

import entity.Ball;
import entity.Shield;

public class PowerUpEffects {

    private static class Explosion {
        float x, y;
        float radius;
        float alpha;
        final float maxRadius;
        private final float expandSpeed = 300; // Tốc độ nở
        private final float fadeSpeed = 1.0f; // Tốc độ mờ

        Explosion(float x, float y, float maxRadius) {
            this.x = x;
            this.y = y;
            this.radius = 1; // Bán kính ban đầu nhỏ hơn để có hiệu ứng "vụt nở"
            this.alpha = 1.0f;
            this.maxRadius = maxRadius;
        }

        void update(float dt) {
            // Tăng bán kính, nhưng không vượt quá maxRadius
            if (radius < maxRadius) {
                radius += expandSpeed * dt;
            } else {
                radius = maxRadius;
            }

            // Giảm độ trong suốt
            alpha -= fadeSpeed * dt;
        }

        void draw(Graphics2D g2d) {
            // Hiệu ứng "tia lửa" ban đầu
            float sparkRadius = radius * 0.8f;
            g2d.setColor(new Color(255, 255, 0, (int) (alpha * 255))); // Màu vàng
            g2d.fillOval((int) (x - sparkRadius), (int) (y - sparkRadius),
                    (int) (sparkRadius * 2), (int) (sparkRadius * 2));

            // Hiệu ứng nổ chính
            g2d.setColor(new Color(255, 100, 0, (int) (alpha * 255))); // Màu cam
            g2d.fillOval((int) (x - radius), (int) (y - radius),
                    (int) (radius * 2), (int) (radius * 2));
        }

        boolean isFinished() {
            return alpha <= 0;
        }
    }

    private final List<Explosion> explosions = new ArrayList<>();

    public void spawnExplosion(float x, float y, float maxRadius) {
        explosions.add(new Explosion(x, y, maxRadius));
    }

    public void update(float dt) {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion e = it.next();
            e.update(dt);
            if (e.isFinished()) {
                it.remove();
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Bật Anti-aliasing để làm mượt các đường cong
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Explosion e : explosions) {
            e.draw(g2d);
        }
    }

    //vẽ bóng lửa
    public void drawFireBallEffect(Graphics g, Ball ball) {
        if (ball.isFireBall()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (int) ball.getX();
            int y = (int) ball.getY();
            int width = (int) ball.getWidth();
            int height = (int) ball.getHeight();
            int size = Constants.BALL_SIZE;

            // Lớp ngoài cùng: Hiệu ứng mờ màu vàng (Yellow glow)
            g2d.setColor(new Color(255, 255, 102, 120)); // Vàng nhạt có độ trong suốt
            g2d.fillOval(x - 4, y - 4, size + 8, size + 8);

            // Lớp giữa: Hiệu ứng cháy màu cam (Orange fire)
            g2d.setColor(new Color(255, 102, 0, 180)); // Cam đậm
            g2d.fillOval(x - 2, y - 2, size + 4, size + 4);

            // Lõi: Lõi đỏ rực (Red core)
            g2d.setColor(Color.RED);
            g2d.fillOval(x, y, size, size);
        }
    }

    public void drawShield(Graphics g, Shield shield) {
        if (shield != null && shield.isActive()) {
            shield.draw(g);
        }
    }
}
