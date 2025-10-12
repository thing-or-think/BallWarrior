package game.skill.active;

import game.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import entity.Ball;
import entity.Brick;
import utils.Vector2D;

import java.util.List;

public class ExplosionSkill extends ActiveSkill {
    private final List<Ball> balls;
    private final List<Brick> bricks;
    private final float explosionRadius = 60f;
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
                    float distance = ballPos.distance(brick.getCenter());
                    if (distance <= explosionRadius) {
                        brick.hit(1);
                    }
                }
            }
            skillEffectManager.spawnExplosion(ballPos.x, ballPos.y, explosionRadius);
        }


        System.out.println("💥 ExplosionSkill activated! All nearby bricks destroyed.");
    }
}
