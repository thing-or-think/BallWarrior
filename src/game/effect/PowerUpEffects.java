package game.effect;

import java.awt.*;

import utils.Constants;

import entity.Ball;
import entity.Shield;

public class PowerUpEffects {

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

    public void drawShield(Graphics2D g, Shield shield) {
        if (shield != null && shield.isActive()) {
            shield.draw(g);
        }
    }
}
