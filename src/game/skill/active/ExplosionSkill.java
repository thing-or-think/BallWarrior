package game.skill.active;

import core.ResourceLoader;
import game.skill.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import entity.Ball;
import entity.Brick;
import utils.Vector2D;

import java.util.List;

public class ExplosionSkill extends ActiveSkill {
    private static final String path = "assets/images/skills/explosion_ball.png";

    private final List<Ball> balls;
    private final List<Brick> bricks;
    private final float explosionRadius = 60f;
    private final SkillEffectManager skillEffectManager;

    public ExplosionSkill(List<Ball> balls,
                          List<Brick> bricks,
                          SkillEffectManager skillEffectManager) {
        super("Explosion",
                ResourceLoader.loadImage(path),
                3.5f);
        this.balls = balls;
        this.bricks = bricks;
        this.skillEffectManager = skillEffectManager;
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
                    float distance = ballPos.distance(brick.getCenter());
                    if (distance <= explosionRadius) {
                        brick.hit(1);
                    }
                }
            }
            skillEffectManager.spawnExplosion(ballPos.x, ballPos.y, explosionRadius);
        }
        System.out.println("💥 ExplosionSkill activated! All nearby bricks destroyed.");
        return true;
    }
}
