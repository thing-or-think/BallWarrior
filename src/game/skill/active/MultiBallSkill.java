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

    @Override
    protected void performAction() {
        if (balls == null || balls.isEmpty()) return;

        List<Ball> newBalls = new ArrayList<>();
        float angle30Deg = (float) Math.toRadians(30); // lệch 30 độ

        for (Ball b : new ArrayList<>(balls)) {
            Vector2D originalVelocity = b.getVelocity();

            if (b.isStuck() || originalVelocity.length() < 1.0f)
                continue;

            // Sao chép vector vận tốc ban đầu
            Vector2D newVelocity1 = new Vector2D(originalVelocity.x, originalVelocity.y);
            Vector2D newVelocity2 = new Vector2D(originalVelocity.x, originalVelocity.y);

            // Xoay trái / phải 30 độ
            newVelocity1.rotate(-angle30Deg);
            newVelocity2.rotate(angle30Deg);

            // Tạo hai bóng mới
            Ball newBall1 = new Ball(b.getX(), b.getY());
            newBall1.setVelocity(newVelocity1.x, newVelocity1.y);
            newBall1.setStuck(false);
            newBalls.add(newBall1);

            Ball newBall2 = new Ball(b.getX(), b.getY());
            newBall2.setVelocity(newVelocity2.x, newVelocity2.y);
            newBall2.setStuck(false);
            newBalls.add(newBall2);

            // Xóa bóng gốc nếu cần
            balls.remove(b);
        }

        balls.addAll(newBalls);
        System.out.println("Multi Ball activated! Total balls: " + balls.size());
    }
}
