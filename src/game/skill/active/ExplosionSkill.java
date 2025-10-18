package game.skill.active;

import game.skill.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import entity.Ball;
import entity.Brick;
import utils.Vector2D;

import java.util.List;

public class ExplosionSkill extends ActiveSkill {
    private final List<Ball> balls;
    private final List<Brick> bricks;
    private final float explosionRadius = 100f;
    private final SkillEffectManager skillEffectManager;

    public ExplosionSkill(List<Ball> balls,
                          List<Brick> bricks,
                          SkillEffectManager skillEffectManager) {
        super("Explosion", 0f);
        this.balls = balls;
        this.bricks = bricks;
        this.skillEffectManager = skillEffectManager;
    }

    @Override
    protected void performAction() {
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
                    }
                }
            }
            skillEffectManager.spawnExplosion(ballPos.x, ballPos.y, explosionRadius);
        }


        System.out.println("💥 ExplosionSkill activated! All nearby bricks destroyed.");
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
