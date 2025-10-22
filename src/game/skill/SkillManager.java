package game.skill;

import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import game.ScoreSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.active.ExplosionSkill;
import game.skill.active.MultiBallSkill;
import game.skill.active.ShieldSkill;
import game.skill.active.FireBallSkill;
import game.collision.CollisionSystem;

import java.awt.event.KeyEvent;
import java.util.List;

public class SkillManager {

    private InputHandler input;
    private List<Ball> balls;
    private CollisionSystem collisionSystem;

    private MultiBallSkill multiBallSkill;
    private ExplosionSkill explosionSkill;
    private ShieldSkill shieldSkill;
    private FireBallSkill fireBallSkill;


    private Shield shield;

    public SkillManager(InputHandler input,
                        Paddle paddle,
                        List<Ball> balls,
                        List<Brick> bricks,
                        ScoreSystem score,
                        CollisionSystem collisionSystem,
                        SkillEffectManager skillEffectManager) {
        this.input = input;
        this.balls = balls;
        this.collisionSystem = collisionSystem;
        this.multiBallSkill = new MultiBallSkill(balls);
        this.explosionSkill = new ExplosionSkill(balls, bricks, skillEffectManager);
        this.shieldSkill = new ShieldSkill(collisionSystem, skillEffectManager);
        this.fireBallSkill = new FireBallSkill(balls, skillEffectManager);
    }

    public void update(float deltaTime) {
        handleInput();
        fireBallSkill.update(deltaTime);
        if (shield != null) {
            shield.update(deltaTime);
            // Hủy đăng ký shield khi hết thời gian
            if (!shield.isActive()) {
                collisionSystem.unregister(shield);
                shield = null; // Xóa tham chiếu
                System.out.println("Shield deactivated!");
            }
        }
    }

    private void handleInput() {
        // Q - Shield
        if (input.isKeyJustPressed(KeyEvent.VK_Q)) {
            shieldSkill.activate();
            shield = shieldSkill.getShield();
        }

        // W - Multi Ball
        if (input.isKeyJustPressed(KeyEvent.VK_W)) {
            multiBallSkill.activate();
        }

        // E - Explosion
        if (input.isKeyJustPressed(KeyEvent.VK_E)) {
            explosionSkill.activate();
        }
        // R - Fire Ball
        if (input.isKeyJustPressed(KeyEvent.VK_R)) {
            fireBallSkill.activate();
        }

    }

    public Shield getShield() {
        return shield;
    }

    /**
     * ⭐ THÊM PHƯƠNG THỨC NÀY
     * GameWorld gọi phương thức này khi tất cả bóng bị mất
     * để hủy kích hoạt skill ngay lập tức.
     */
    public void deactivateFireBall() {
        if (fireBallSkill != null) {
            fireBallSkill.forceDeactivate();
        }
    }
}
