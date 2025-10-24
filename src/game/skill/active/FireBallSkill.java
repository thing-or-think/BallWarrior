package game.skill.active;

import core.ResourceLoader;
import entity.Ball;
import game.skill.base.ActiveSkill;
import game.skill.effect.FireBallVisualEffect;
import game.skill.effect.SkillEffectManager;

import java.awt.event.KeyEvent;
import java.util.List;

public class FireBallSkill extends ActiveSkill {
    private final List<Ball> balls;
    private final SkillEffectManager skillEffectManager;
    private static final String path = "assets/images/skills/fire_ball.png";
    // --- Logic Power-Up thủ công (Tích hợp) ---
    protected float duration;
    protected float timer;
    protected boolean active = false;

    // Thời gian duy trì Fire Ball
    private static final float FIREBALL_DURATION = 10.0f;
    private static final float FIREBALL_COOLDOWN = 0.0f;

    public FireBallSkill(List<Ball> balls, SkillEffectManager skillEffectManager) {
        super("FIRE_BALL",
                ResourceLoader.loadImage(path),
                FIREBALL_COOLDOWN,
                KeyEvent.VK_R,
                10,
                FIREBALL_DURATION);
        this.balls = balls;
        this.skillEffectManager = skillEffectManager;
        this.duration = FIREBALL_DURATION;
    }

    @Override
    public void update(float deltaTime) {
        // 1. Logic đếm giờ duy trì (Power-Up duration)
        if (active) {
            timer += deltaTime;
            if (timer >= duration) {
                timer = 0;
                active = false;
                isReady = true; // Sẵn sàng lại ngay lập tức
                onDeactivate(); // Hủy kích hoạt hiệu ứng
            }
        }

        // 2. Logic cooldown chuẩn của ActiveSkill (chỉ chạy khi không active)
        super.update(deltaTime);
    }

    @Override
    protected boolean performAction() {
        // Hành động khi kích hoạt
        if (balls == null || balls.isEmpty()) {
            return false;
        }

        for (Ball ball : balls) {
            ball.setFireBall(true);
            skillEffectManager.addFireBallVisualEffect(new FireBallVisualEffect(ball));
        }
        System.out.println("Fire Ball activated! Balls gain penetration ability for " + duration + "s.");
        return true;

    }

    protected void onDeactivate() {
        if (balls == null) return;

        for (Ball ball : balls) {
            ball.setFireBall(false);
        }
        System.out.println("Fire Ball deactivated. Balls return to normal.");
    }

    /**
     * Tắt cưỡng bức (GameWorld gọi khi tất cả bóng bị mất).
     */
    public void forceDeactivate() {
        if (active) {
            timer = 0;
            active = false;
            isReady = true; // ĐẶT LẠI TRẠNG THÁI SẴN SÀNG NGAY LẬP TỨC
            cooldownTimer = 0; // Đảm bảo cooldown reset về 0
            onDeactivate();
        }
    }

    public boolean isActive() {
        return active;
    }
}
