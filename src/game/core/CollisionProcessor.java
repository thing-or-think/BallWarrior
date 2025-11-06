package game.core;

import core.AudioService;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import entity.*;
import game.ScoreSystem;

import java.util.ArrayList; // THÊM IMPORT
import java.util.Iterator;
import java.util.List;

/**
 * Xử lý collision hậu va chạm.
 * [SỬA LỖI FIREBALL V2.0 - Dùng Ignore List]
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
     * [SỬA] Duyệt qua tất cả ball và xử lý va chạm.
     * Sử dụng một vòng lặp lồng nhau và một "danh sách bỏ qua" (ignore list)
     * để xử lý chính xác các va chạm xuyên thấu (piercing) của Fireball.
     */
    public void processCollisions() {
        List<Ball> balls = entities.getBalls();
        Iterator<Ball> it = balls.iterator();

        // Danh sách tạm thời để lưu các entity bị Fireball phá hủy TRONG frame này
        List<Entity> ignoredEntities = new ArrayList<>();

        while (it.hasNext()) {
            Ball ball = it.next();
            // Xóa danh sách bỏ qua cho mỗi quả bóng mới
            ignoredEntities.clear();

            // Bắt đầu vòng lặp xử lý nhiều va chạm cho MỘT quả bóng
            while (true) {

                // 1. Tìm va chạm gần nhất, bỏ qua các gạch đã bị phá
                CollisionResult result = collisionSystem.findNearestCollision(ball, ignoredEntities);
                // 2. Nếu không còn va chạm nào, thoát vòng lặp
                if (result == null) {
                    break; // Thoát khỏi while(true), xử lý bóng tiếp theo
                }

                Entity entity = result.getEntity();

                // 3. Phân loại va chạm
                boolean isPiercingCollision = false; // Cờ

                if (ball.isFireBall() && entity instanceof Brick brick) {
                    if (brick.getType() != Brick.Type.BEDROCK) {
                        isPiercingCollision = true;
                    }
                }

                if (isPiercingCollision) {
                    // --- TRƯỜNG HỢP 1: XUYÊN GẠCH (Piercing) ---
                    Brick brick = (Brick) entity;
                    brick.hit(brick.getMaxHealth()); // Phá gạch

                    if (brick.isDestroyed()) {
                        AudioService.playSound("fizz.wav");


                        // (Logic spawn orb, cộng điểm, combo)
                        ManaOrb orb = orbSpawner.trySpawn(brick);
                        if (orb != null) entities.addManaOrb(orb);
                        scoreSystem.addScore(brick.getScoreValue());
                        scoreSystem.increaseCombo(0.25f);
                        collisionSystem.unregister(brick);
                        // Thêm gạch này vào danh sách bỏ qua
                        ignoredEntities.add(brick);
                    }

                    continue;

                } else {
                    // --- TRƯỜNG HỢP 2: VA CHẠM RẮN (Solid) ---
                    // (Bóng thường vs Gạch, Fireball vs Paddle, Fireball vs Bedrock, v.v.)

                    // Gọi resolveCollision để NẢY LẠI
                    AudioService.playSound("hit_brick.wav");
                    if (collisionSystem.resolveCollision(ball, result)) {

                        if (entity instanceof Brick brick) {

                            if (brick.isDestroyed()) { // Chỉ chạy nếu là gạch thường
                                AudioService.playSound("broken.wav");
                                ManaOrb orb = orbSpawner.trySpawn(brick);
                                if (orb != null) entities.addManaOrb(orb);
                                scoreSystem.addScore(brick.getScoreValue());
                                scoreSystem.increaseCombo(0.25f);
                                collisionSystem.unregister(brick);
                            }
                        } else {
                            // (Va chạm với Paddle hoặc Shield)
                            if(entity instanceof Shield) {
                                AudioService.playSound("ball_shield.wav");
                            }
                            else {
                                AudioService.playSound("Ball-Paddle.wav");
                            }
                            scoreSystem.resetCombo();
                        }
                    }

                    // Vì đây là va chạm "rắn", bóng đã đổi hướng.
                    // Dừng xử lý va chạm cho bóng này.
                    break;
                }
            }
        }
    }


}
