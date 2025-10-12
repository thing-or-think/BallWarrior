package game.skill.active;

import entity.Ball;
import game.skill.base.ActiveSkill;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class MultiBallSkill extends ActiveSkill {
    private List<Ball> balls;

    public MultiBallSkill(List<Ball> balls) {
        super("MULTI_BALL", 0f);
        this.balls = balls;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    protected void performAction() {
        if (balls == null || balls.isEmpty()) {
            return;
        }

        List<Ball> newBalls = new ArrayList<>();
        float angle90Deg = (float) Math.toRadians(90);

        // Tạo bản sao để tránh ConcurrentModificationException
        for (Ball b : new ArrayList<>(balls)) {
            Vector2D originalVelocity = b.getVelocity();

            // Bỏ qua nếu bóng đang dính hoặc tốc độ quá chậm
            if (b.isStuck() || originalVelocity.length() < 1.0f) {
                continue;
            }

            // Tạo hai vector mới từ hướng ban đầu
            Vector2D newVelocity1 = new Vector2D(originalVelocity.x, originalVelocity.y);
            Vector2D newVelocity2 = new Vector2D(originalVelocity.x, originalVelocity.y);

            // Xoay trái / phải 90 độ
            newVelocity1.rotate(-angle90Deg);
            newVelocity2.rotate(angle90Deg);

            // Tạo hai bóng mới với vận tốc mới
            Ball newBall1 = new Ball(b.getX(), b.getY());
            newBall1.setVelocity(newVelocity1.x, newVelocity1.y);
            newBall1.setStuck(false);
            newBalls.add(newBall1);

            Ball newBall2 = new Ball(b.getX(), b.getY());
            newBall2.setVelocity(newVelocity2.x, newVelocity2.y);
            newBall2.setStuck(false);
            newBalls.add(newBall2);

            // Có thể bỏ bóng gốc nếu muốn (như code trước)
             balls.remove(b);
        }

        balls.addAll(newBalls);
        System.out.println("Multi Ball activated! Total balls: " + balls.size());
    }
}
