package game.skill.active;

import core.ResourceLoader;
import entity.Ball;
import game.skill.base.ActiveSkill;
import game.skill.effect.SkillEffectManager;
import game.skill.effect.FireBallVisualEffect;
import utils.Vector2D;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MultiBallSkill extends ActiveSkill {
    private static final String path = "assets/images/skills/multiple_ball.png";
    private List<Ball> balls;
    private final SkillEffectManager skillEffectManager;

    public MultiBallSkill(List<Ball> balls, SkillEffectManager skillEffectManager) {
        super("MULTI_BALL",
                ResourceLoader.loadImage(path),
                5f,
                KeyEvent.VK_W,
                6);
        this.balls = balls;
        this.skillEffectManager = skillEffectManager;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    @Override
    protected boolean performAction() {
        if (balls == null || balls.isEmpty()) {
            return false;
        }

        List<Ball> newBalls = new ArrayList<>();
        float angle30Deg = (float) Math.toRadians(30); // lệch 30 độ

        for (Ball b : new ArrayList<>(balls)) {
            Vector2D originalVelocity = b.getVelocity();

            if (b.isStuck() || originalVelocity.length() < 1.0f) {
                continue;
            }

            boolean wasFireBall = b.isFireBall();

            // Sao chép vector vận tốc ban đầu
            Vector2D newVelocity1 = new Vector2D(originalVelocity.x, originalVelocity.y);
            Vector2D newVelocity2 = new Vector2D(originalVelocity.x, originalVelocity.y);

            // Xoay trái / phải 30 độ
            newVelocity1.rotate(-angle30Deg);
            newVelocity2.rotate(angle30Deg);

            // Xóa bóng gốc (BẮT BUỘC để tránh lỗi)
            balls.remove(b);

            // Tắt Fire Ball trên bóng gốc để hiệu ứng cũ tự hủy (nếu có)
            if (wasFireBall) {
                b.setFireBall(false); // Cần tắt để hiệu ứng VisualEffect của bóng cũ bị xóa
            }

            // Tạo hai bóng mới
            Ball newBall1 = new Ball(b.getX(), b.getY());
            newBall1.setVelocity(newVelocity1.x, newVelocity1.y);
            newBall1.setStuck(false);
            newBall1.setFireBall(wasFireBall);
            if (wasFireBall) {
                // Đăng ký hiệu ứng cho bóng mới 1
                skillEffectManager.addFireBallVisualEffect(new FireBallVisualEffect(newBall1));
            }
            newBalls.add(newBall1);

            Ball newBall2 = new Ball(b.getX(), b.getY());
            newBall2.setVelocity(newVelocity2.x, newVelocity2.y);
            newBall2.setStuck(false);
            newBall2.setFireBall(wasFireBall);
            if (wasFireBall) {
                // Đăng ký hiệu ứng cho bóng mới 2
                skillEffectManager.addFireBallVisualEffect(new FireBallVisualEffect(newBall2));
            }
            newBalls.add(newBall2);
        }

        balls.addAll(newBalls);
        System.out.println("Multi Ball activated! Total balls: " + balls.size());
        return true;
    }
}
