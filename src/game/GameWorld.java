package game;

import core.InputHandler;
import entity.*;
import game.collision.*;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;
    private SkillManager skillManager;
    private SkillEffectManager skillEffectManager;

    public GameWorld(InputHandler input) {
        init(input);
    }

    private void init(InputHandler input) {
        paddle = new Paddle(Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30, input);
        balls = new ArrayList<>();
        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        levelManager.load("assets/levels/level1.json");

        LevelData level = levelManager.getCurrentLevel();
        bricks = LevelBuilder.buildBricks(level);

        collisionSystem = new CollisionSystem(paddle);
        skillEffectManager = new SkillEffectManager();
        skillManager = new SkillManager(input, paddle, balls, bricks, scoreSystem, collisionSystem, skillEffectManager);

        collisionSystem.register(paddle);
        for (Brick brick : bricks) collisionSystem.register(brick);

        resetBall();
    }

    public void update(float deltaTime) {
        skillManager.update(deltaTime);
        paddle.update();

        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();

            if (ball.isStuck()) {
                ball.reset(paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE);
                if (paddle.getInput().isKeyJustPressed(KeyEvent.VK_SPACE)) {
                    ball.launch();
                }
            } else {
                ball.update();
            }

            CollisionResult result = collisionSystem.findNearestCollision(ball);
            if (result != null) handleCollision(ball, result);
        }

        bricks.removeIf(Brick::isDestroyed);
        balls.removeIf(ball -> ball.getY() > Constants.HEIGHT);

        if (balls.isEmpty()) {
            skillManager.deactivateFireBall();
            scoreSystem.loseLife();
            resetBall();
        }

        skillEffectManager.update(deltaTime);
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
                    scoreSystem.addScore(brick.getScoreValue());
                    scoreSystem.increaseCombo(0.25f);
                    collisionSystem.unregister(brick);
                }
                // KHÔNG gọi resolveCollision để bóng xuyên qua.
            } else {
                // --- XỬ LÝ BẬT NẢY (Bóng thường vs mọi gạch, HOẶC Fire Ball vs Bedrock) ---

                // collisionSystem.resolveCollision:
                // 1. Nếu là bóng thường: Xử lý bật nảy
                // 2. Nếu là Fire Ball + Bedrock: Xử lý bật nảy, và brick.hit() sẽ bị Bedrock bỏ qua.
                if (collisionSystem.resolveCollision(ball, result)) {
                    // Cập nhật điểm và xóa gạch (chỉ áp dụng nếu gạch vỡ)
                    if (brick.isDestroyed()) {
                        scoreSystem.addScore(brick.getScoreValue());
                        scoreSystem.increaseCombo(0.25f);
                        collisionSystem.unregister(brick);
                    }
                }
            }
        } else if (entity instanceof Shield || entity instanceof Paddle) {
            if (collisionSystem.resolveCollision(ball, result)) {
                scoreSystem.resetCombo();
            }
        }
    }

    public void render(Graphics2D g2) {
        paddle.draw(g2);
        skillEffectManager.draw(g2);
        for (Brick brick : bricks) if (!brick.isDestroyed()) brick.draw(g2);
        for (Ball ball : balls) ball.draw(g2);
    }

    private void resetBall() {
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
        balls.clear();
        balls.add(ball);
        scoreSystem.resetCombo();
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public void forceUpdateGameAssets() {
        // Tải lại Skin vào cache static
        Ball.loadEquippedAssets();
        Paddle.loadEquippedAssets();

        // Cập nhật đối tượng đang tồn tại
        for (Ball ball : balls) {
            if (ball != null) {
                ball.setImg(Ball.equippedBallImage);
                ball.setColor(Ball.equippedBallColor);

            }
        }
        if (paddle != null) {
            paddle.setImg(Paddle.equippedPaddleImage);
            paddle.setColor(Paddle.equippedPaddleColor);
        }
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }
}
