package game.skill.active;

import core.ResourceLoader;
import core.AudioService;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import entity.Ball;
import entity.Brick;
import utils.Vector2D;

import java.awt.event.KeyEvent;
import java.util.List;

public class ExplosionSkill extends ActiveSkill {
    private static final String path = "assets/images/skills/explosion_ball.png";

    private final List<Ball> balls;
    private final List<Brick> bricks;
    private final float explosionRadius = 100f;
    private final SkillEffectManager skillEffectManager;
    private final CollisionSystem collisionSystem;

    public ExplosionSkill(List<Ball> balls,
                          List<Brick> bricks,
                          SkillEffectManager skillEffectManager,
                          CollisionSystem collisionSystem) {
        super("Explosion",
                ResourceLoader.loadImage(path),
                3.5f,
                KeyEvent.VK_E,
                3);
        this.balls = balls;
        this.bricks = bricks;
        this.skillEffectManager = skillEffectManager;
        this.collisionSystem = collisionSystem;
    }

    @Override
    protected boolean performAction() {
        if (balls.isEmpty()) {
            return false;
        }
        for (Ball ball : balls) {
            Vector2D ballPos = ball.getCenter();
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    Vector2D brickPos = brick.getCenter();
                    float dx = Math.max(Math.abs(ballPos.x - brickPos.x) - brick.getHeight() / 2, 0);
                    float dy = Math.max(Math.abs(ballPos.y - brickPos.y) - brick.getHeight() / 2, 0);
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance < explosionRadius) {
                        brick.hit(getDamageByDistance(distance,explosionRadius));

                        if(brick.isDestroyed()) {
                            // score + combo
                            scoreSystem.addScore(brick.getScoreValue());
                            scoreSystem.increaseCombo(0.25f);
                            collisionSystem.unregister(brick);
                        }
                    }
                }
            }
            skillEffectManager.spawnExplosion(ballPos.x, ballPos.y, explosionRadius);
        }
        System.out.println("💥 ExplosionSkill activated! All nearby bricks destroyed.");
        return true;
    }

    // GHI ĐÈ PHƯƠNG THỨC PHÁT ÂM THANH
    @Override
    protected void playActivationSound() {
        // âm thanh nổ
        AudioService.playSound("explosion.wav");
    }

    /**
     * Trả về lượng sát thương dựa trên khoảng cách đến tâm nổ
     */
    public int getDamageByDistance(float distance, float radius) {
        if (distance <= radius / 3f) return 5;       // gần tâm
        if (distance <= (2f * radius) / 3f) return 3; // trung bình
        return 1;                                    // xa
    }
}
