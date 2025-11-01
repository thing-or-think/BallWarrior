package game;

import core.InputHandler;
import data.SkinData;
import entity.*;
import entity.Ball;
import game.core.*;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;

import java.awt.*;
import java.util.List; // Thêm import

public class GameWorld {
    private final EntityManager entities;
    private final CollisionProcessor collisionProcessor;
    private final SkillManager skillManager;
    private final SkillEffectManager skillEffectManager;
    private final ScoreSystem scoreSystem;

    private final LevelManager levelManager;
    private final CollisionSystem collisionSystem; // Giữ tham chiếu để reset

    private int breakableBrickCount = 0;
    private boolean isLevelWon = false;

    public GameWorld(InputHandler input) {
        GameInitializer init = new GameInitializer();
        init.initialize(input);

        this.entities = init.getEntityManager();
        this.collisionSystem = init.getCollisionSystem(); // <-- Lấy tham chiếu này
        this.skillManager = init.getSkillManager();
        this.skillEffectManager = init.getSkillEffectManager();
        this.scoreSystem = init.getScoreSystem();
        this.levelManager = init.getLevelManager(); // <-- Lấy LevelManager
        // collision processor wired with orbSpawner
        this.collisionProcessor = new CollisionProcessor(collisionSystem, init.getOrbSpawner(), entities, scoreSystem);
    }

    /**
     * [MỚI] Phương thức để reset và tải một màn chơi mới
     * @param levelPath Đường dẫn đến file JSON của level
     */
    public void resetAndLoadLevel(String levelPath) {
        // 1. Tải dữ liệu level mới
        LevelData level = levelManager.load(levelPath);
        if (level == null) {
            System.err.println("KHÔNG THỂ TẢI LEVEL: " + levelPath);
            return;
        }

        // --- BẮT ĐẦU SỬA ---
        // 1. Reset trạng thái thắng và bộ đếm
        this.isLevelWon = false;
        this.breakableBrickCount = 0;

        // 2. Dọn dẹp trạng thái cũ
        scoreSystem.reset();
        entities.getBalls().clear(); // Xóa bóng cũ

        // Dọn gạch cũ khỏi EntityManager và CollisionSystem
        List<Brick> oldBricks = entities.getBricks();
        for (Brick b : oldBricks) {
            collisionSystem.unregister(b);
        }
        oldBricks.clear();

        // 3. Tải dữ liệu level mới
        List<Brick> newBricks = LevelBuilder.buildBricks(level);
        entities.setBricks(newBricks); // Đặt gạch mới cho EntityManager

        // Đếm gạch CÓ THỂ PHÁ và đăng ký
        for (Brick b : newBricks) {
            collisionSystem.register(b);
            if (b.getType() != Brick.Type.BEDROCK) {
                this.breakableBrickCount++; // <-- Đếm gạch
            }
        }

        // 4. Đăng ký gạch mới vào hệ thống va chạm
        for (Brick b : newBricks) {
            collisionSystem.register(b);
        }

        // 5. Cập nhật SkillManager nếu nó giữ tham chiếu đến bricks
        // (Trong code merge_feature, nó được inject qua EntityManager nên tự động cập nhật)

        // 6. Reset lại bóng
        entities.spawnNewBallAtPaddle();
    }


    public void update(float deltaTime) {
        if (isLevelWon) return;
        skillManager.update(deltaTime);
        entities.updateAll(deltaTime);

        collisionProcessor.processCollisions();
        entities.getManaOrbs().stream()
                .filter(orb -> !orb.isAlive()) // already picked
                .forEach(orb -> scoreSystem.addMana(orb.getManaAmount()));
        entities.cleanupDestroyed();

        if (entities.noBallsRemaining()) {
            scoreSystem.loseLife();
            entities.spawnNewBallAtPaddle();
        }

        if (this.breakableBrickCount > 0) {
            // Đếm số gạch CÓ THỂ PHÁ còn sống
            long liveBreakableBricks = entities.getBricks().stream()
                    .filter(b -> b.getType() != Brick.Type.BEDROCK && b.isAlive())
                    .count();

            if (liveBreakableBricks == 0) {
                this.isLevelWon = true; // Đặt cờ thắng
                System.out.println("LEVEL WON!");
            }
        }

        skillEffectManager.update(deltaTime);
    }

    public void render(Graphics2D g2) {
        entities.render(g2);
        skillEffectManager.draw(g2);
    }

    public void forceUpdateGameAssets(SkinData ballSkin, SkinData paddleSkin) {
        // refresh static cache
        Ball.setSkin(ballSkin);
        Paddle.setSkin(paddleSkin);

        // update existing instances
        for (Ball ball : entities.getBalls()) {
            if (ball != null) {
                ball.setImg(Ball.equippedBallImage);
                ball.setColor(Ball.equippedBallColor);
            }
        }
        Paddle paddle = entities.getPaddle();
        if (paddle != null) {
            paddle.setImg(Paddle.equippedPaddleImage);
            paddle.setColor(Paddle.equippedPaddleColor);
        }
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public boolean isLevelWon() {
        return isLevelWon;
    }
}
