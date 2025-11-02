package game.core;

import entity.Ball;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import entity.*;
import game.ScoreSystem;
import game.SoundManager;

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
    private final SoundManager soundManager;

    public CollisionProcessor(CollisionSystem collisionSystem,
                              OrbSpawner orbSpawner,
                              EntityManager entities,
                              ScoreSystem scoreSystem,
                              SoundManager soundManager) {
        this.collisionSystem = collisionSystem;
        this.orbSpawner = orbSpawner;
        this.entities = entities;
        this.scoreSystem = scoreSystem;
        this.soundManager = soundManager;
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
            // 1. Xác định gạch có phải là Bedrock không
            // Dùng getType() == Brick.Type.BEDROCK
            boolean isBedrock = brick.getType() == Brick.Type.BEDROCK;

            // 2. Fire Ball chỉ xuyên phá nếu nó đang active VÀ gạch KHÔNG phải là Bedrock
            boolean shouldPierce = ball.isFireBall() && !isBedrock;

            if (shouldPierce) {
                // --- XỬ LÝ XUYÊN PHÁ (Fire Ball vs Gạch thường) ---

                // Gây sát thương tối đa, đảm bảo gạch phá hủy được sẽ vỡ ngay lập tức.
                // Hàm hit() trong Brick.java đã tự return nếu là Bedrock, nên an toàn.
                brick.hit(brick.getMaxHealth());

                // Cập nhật điểm và xóa gạch nếu bị phá hủy
                if (brick.isDestroyed()) {
                    // PHÁT ÂM THANH: FIZZ (âm thanh xuyên phá/làm tan chảy)
                    SoundManager.play(SoundManager.FIZZ);

                    // spawn orb via OrbSpawner
                    ManaOrb orb = orbSpawner.trySpawn(brick);
                    if (orb != null) {
                        entities.addManaOrb(orb);
                    }
                    // score + combo
                    scoreSystem.addScore(brick.getScoreValue());
                    scoreSystem.increaseCombo(0.25f);
                    collisionSystem.unregister(brick);
                }
                // KHÔNG gọi resolveCollision để bóng xuyên qua.
            } else {
                if (collisionSystem.resolveCollision(ball, result)) {
                    // PHÁT ÂM THANH: HIT_BRICK (Âm thanh va chạm)
                    // Chỉ phát nếu gạch không phải Bedrock (Bedrock không phát tiếng nứt)
                    if (!isBedrock) {
                        SoundManager.play(SoundManager.HIT_BRICK);
                    }

                    if (brick.isDestroyed()) {
                        // phát âm thanh vỡ
                        SoundManager.play(SoundManager.BROKEN);
                        // spawn orb via OrbSpawner
                        ManaOrb orb = orbSpawner.trySpawn(brick);
                        if (orb != null) {
                            entities.addManaOrb(orb);
                        }
                        // score + combo
                        scoreSystem.addScore(brick.getScoreValue());
                        scoreSystem.increaseCombo(0.25f);
                        collisionSystem.unregister(brick);
                    }
                }
            }
        } else if (entity instanceof Shield || entity instanceof Paddle) {
            if (collisionSystem.resolveCollision(ball, result)) {
                if(entity instanceof Shield) {
                    SoundManager.play(SoundManager.BALL_SHIELD);
                }
                scoreSystem.resetCombo();
            }
        }
    }
}
