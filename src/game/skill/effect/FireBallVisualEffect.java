package game.skill.effect;

import entity.Ball;
import utils.Vector2D;

import java.awt.*;
import java.util.LinkedList;
import utils.Constants; // Giả định Constants có sẵn

public class FireBallVisualEffect {

    private final Ball ball;
    // Lưu trữ điểm vệt lửa (vì không thể lưu trong Ball.java)
    private final LinkedList<Vector2D> trailPoints;

    private static final int TRAIL_LENGTH = 10;
    private static final int TRAIL_POINT_INTERVAL = 3; // Lấy điểm sau mỗi X update
    private int updateCounter = 0;
    private boolean isActive = true;

    public FireBallVisualEffect(Ball ball) {
        this.ball = ball;
        this.trailPoints = new LinkedList<>();
        if (ball != null) {
            // Giả định Ball có phương thức getCenter()
            this.trailPoints.add(ball.getCenter());
        }
    }

    public void update(float deltaTime) {
        // Tự hủy nếu bóng không còn là Fire Ball (dựa trên ball.isFireBall())
        // hoặc bóng đã bị xóa khỏi màn hình (ball.getY() > Constants.HEIGHT)
        if (ball == null || !ball.isFireBall() || !ball.isAlive()) {
            isActive = false;
            return;
        }

        // Cập nhật điểm vệt lửa
        updateCounter++;
        if (updateCounter >= TRAIL_POINT_INTERVAL) {
            trailPoints.addFirst(ball.getCenter());
            if (trailPoints.size() > TRAIL_LENGTH) {
                trailPoints.removeLast();
            }
            updateCounter = 0;
        }
    }

    public void draw(Graphics2D g) {
        if (!isActive || trailPoints.isEmpty()) return;

        // 1. Vẽ Vệt Lửa (Trail)
        for (int i = trailPoints.size() - 1; i >= 0; i--) {
            Vector2D p = trailPoints.get(i);

            // Tính độ mờ (alpha) và kích thước (size) cho hiệu ứng mờ dần
            float alphaFactor = (float) i / (trailPoints.size() - 1);
            int alpha = (int) (255 * (1.0f - alphaFactor));
            alpha = Math.max(0, Math.min(255, alpha));

            // Kích thước giảm dần theo alphaFactor
            int currentSize = (int) (ball.getWidth() * (0.5f + (1.0f - alphaFactor) * 0.5f));

            g.setColor(new Color(255, 100, 0, alpha)); // Màu cam lửa
            g.fillOval((int) p.x - currentSize / 2, (int) p.y - currentSize / 2, currentSize, currentSize);
        }

        // 2. Vẽ Viền Đỏ (Border/Glow)
        // Hiệu ứng này sẽ được vẽ trước quả bóng, tạo cảm giác một lớp glow (phát sáng)
        int borderThickness = 2;
        int size = ball.getWidth();
        g.setColor(Color.RED);

        g.fillOval((int) ball.getX() - borderThickness,
                (int) ball.getY() - borderThickness,
                size + 2 * borderThickness,
                size + 2 * borderThickness);
    }

    public boolean isActive() {
        return isActive;
    }

    public Ball getBall() {
        return ball;
    }
}
