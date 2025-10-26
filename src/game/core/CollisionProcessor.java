package game.core;

import entity.ball.Ball;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import entity.*;
import game.ScoreSystem;

import java.util.Iterator;
import java.util.List;

/**
 * Xử lý collision hậu va chạm, tách responsibility ra khỏi GameWorld.
 */
public class CollisionProcessor {

    private final CollisionSystem collisionSystem;
    private final OrbSpawner orbSpawner;
    private final EntityManager entities;
    private final ScoreSystem scoreSystem;

    public CollisionProcessor(CollisionSystem collisionSystem,
                              OrbSpawner orbSpawner,
                              EntityManager entities,
                              ScoreSystem scoreSystem) {
        this.collisionSystem = collisionSystem;
        this.orbSpawner = orbSpawner;
        this.entities = entities;
        this.scoreSystem = scoreSystem;
    }

    /**
     * Duyệt qua tất cả ball và xử lý va chạm gần nhất (nếu có).
     * Giữ logic giống trước: spawn orb, add score, combo, unregister brick,...
     */
    public void processCollisions() {
        List<Ball> balls = entities.getBalls();
        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();
            CollisionResult result = collisionSystem.findNearestCollision(ball);
            if (result != null) {
                handleCollision(ball, result);
            }
        }
    }

    private void handleCollision(Ball ball, CollisionResult result) {
        Entity entity = result.getEntity();

        if (entity instanceof Brick brick) {
            if (collisionSystem.resolveCollision(ball, result)) {
                if (brick.isDestroyed()) {
                    // spawn orb via OrbSpawner
                    ManaOrb orb = orbSpawner.trySpawn(brick);
                    if (orb != null) {
                        entities.addManaOrb(orb);
                    }
                    // score + combo
                    scoreSystem.addScore(brick.getScoreValue());
                    scoreSystem.increaseCombo(0.5f);
                    collisionSystem.unregister(brick);
                }
            }
        } else if (entity instanceof Shield || entity instanceof Paddle) {
            if (collisionSystem.resolveCollision(ball, result)) {
                scoreSystem.resetCombo();
            }
        }
    }
}
