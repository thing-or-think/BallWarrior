package game;

import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import game.effect.SkillEffectManager;
import game.skill.active.ExplosionSkill;
import game.skill.active.MultiBallSkill;
import utils.Vector2D;
import utils.Constants;
import game.collision.CollisionSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PowerUpSystem {

    private InputHandler input;
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private PowerUpEffects powerUpEffects;
    private ScoreSystem score;
    private CollisionSystem collisionSystem;
    private MultiBallSkill multiBallSkill;
    private ExplosionSkill explosionSkill;

    // Các biến trạng thái của power-up
    private boolean fireBallActive = false;
    private boolean shieldActive = false;

    private Shield shield;

    public PowerUpSystem(InputHandler input,
                         Paddle paddle,
                         List<Ball> balls,
                         List<Brick> bricks,
                         PowerUpEffects powerUpEffects,
                         ScoreSystem score,
                         CollisionSystem collisionSystem,
                         SkillEffectManager skillEffectManager) {
        this.input = input;
        this.paddle = paddle;
        this.balls = balls;
        this.bricks = bricks;
        this.powerUpEffects = powerUpEffects;
        this.score = score;
        this.collisionSystem = collisionSystem;
        this.multiBallSkill = new MultiBallSkill(balls);
        this.explosionSkill = new ExplosionSkill(balls, bricks, skillEffectManager);
    }

    public void update(float deltaTime) {
        handleInput();
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
        if (input.isQPressed() && (shield == null || !shield.isActive())) {
            float shieldWidth = Constants.WIDTH;
            float shieldHeight = 10;
            float x = 0;
            float y = Constants.HEIGHT - 40;
            float duration = 5f; // giây

            shield = new Shield(x, y, (int) shieldWidth, (int) shieldHeight, duration);
            shield.activate();
            collisionSystem.register(shield);
            System.out.println("Shield activated!");
        }

        // W - Multi Ball
        if (input.isWPressed()) {
            multiBallSkill.activate();
        }

        // E - Explosion
        if (input.isEPressed()) {
            explosionSkill.activate();
        }

        // R - Fire Ball
        if (input.isRPressed()) {
            activateFireBall();
        }
    }

    private int getDamageByDistance(float distance, float radius) {
        if (distance <= radius / 3f) return 5;   // gần tâm
        if (distance <= (2f * radius) / 3f) return 3; // trung bình
        return 1; // xa
    }

    private void activateFireBall() {
        if (!fireBallActive) {
            for (Ball b : balls) {
                b.setFireBall(true);
            }
            fireBallActive = true;
        }
    }

    public void deactivateFireBall() {
        if (fireBallActive) {
            for (Ball b : balls) {
                b.setFireBall(false);
            }
            fireBallActive = false;
        }
    }

    public Shield getShield() {
        return shield;
    }
}
