package game.skill.active;

import entity.Ball;
import game.skill.base.PowerUpSkill;
import game.skill.effect.FireBallVisualEffect;
import game.skill.effect.SkillEffectManager;
import java.util.List;

public class FireBallSkill extends PowerUpSkill {
    private final List<Ball> balls;
    private final SkillEffectManager skillEffectManager;

    // Thời gian duy trì Fire Ball
    private static final float FIREBALL_DURATION = 10.0f;

    public FireBallSkill(List<Ball> balls, SkillEffectManager skillEffectManager) {
        super("FIRE_BALL", FIREBALL_DURATION);
        this.balls = balls;
        this.skillEffectManager = skillEffectManager;
    }

    @Override
    public void activate() {
        if (isReady) {
            // Bắt đầu đếm giờ và đặt cờ active/isReady
            this.isReady = false;
            this.active = true;
            this.timer = 0;

            onActivate();
        }
    }

    private void onActivate() {
        if (balls == null || balls.isEmpty()) return;

        for (Ball ball : balls) {
            // Giả định ball.setFireBall(true) đã được thêm ở bước trước
            ball.setFireBall(true);

            // Đăng ký hiệu ứng Vệt lửa và Viền đỏ cho TỪNG QUẢ BÓNG
            skillEffectManager.addFireBallVisualEffect(new FireBallVisualEffect(ball));
        }
        System.out.println("🔥 Fire Ball activated! Balls gain penetration ability for " + duration + "s.");
    }


    /**
     * Hành động khi hết thời gian và skill bị hủy kích hoạt
     */
    @Override
    protected void onDeactivate() {
        if (balls == null) return;

        // Hủy kích hoạt Fire Ball cho TẤT CẢ các quả bóng
        for (Ball ball : balls) {
            ball.setFireBall(false);
        }
        System.out.println("Fire Ball deactivated. Balls return to normal.");
    }

    public void forceDeactivate() {
        if (active) {
            timer = 0;
            active = false;
            isReady = true; // ✅ ĐẶT LẠI TRẠNG THÁI SẴN SÀNG
            onDeactivate(); // Tắt trạng thái Fire Ball trên các bóng còn lại
        }
    }

    // Ghi đè phương thức update để duy trì đồng hồ đếm ngược (kế thừa từ PowerUpSkill)
    // @Override
    // public void update(float deltaTime) {...} // Không cần ghi đè vì PowerUpSkill đã có logic update
}
