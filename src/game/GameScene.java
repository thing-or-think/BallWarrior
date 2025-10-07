package game;

import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import ui.HUD;
import ui.base.Scene;
import utils.Constants;

import java.awt.*;
import java.util.List;

public class GameScene extends Scene {

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;

    // ==== CONSTRUCTOR ========================================================
    public GameScene(InputHandler input) {
        super("Game", input);
        initUI();
    }

    // ==== ABSTRACT IMPLEMENTATIONS ==========================================

    @Override
    protected void initUI() {
        // Paddle ở giữa đáy màn
        paddle = new Paddle(
                Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                input
        );

        // Đặt bóng ở trên paddle
        resetBall();

        // Hệ thống điểm, level, collision
        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        bricks = levelManager.load("assets/levels/level1.txt");

        collisionSystem = new CollisionSystem(paddle);
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }

    }

    @Override
    protected void update() {
        paddle.update();
        ball.update();

        // Va chạm bóng
        CollisionResult result = collisionSystem.findNearestCollision(ball);
        if (collisionSystem.resolveCollision(ball, result)) {
            if (result.getEntity() instanceof Brick brick) {
                if (brick.isDestroyed()) {
                    scoreSystem.addScore(100);
                    collisionSystem.unregister(brick);
                }
            }
        }

        // Mất mạng nếu bóng rơi khỏi màn
        if (ball.getY() > Constants.HEIGHT) {
            scoreSystem.loseLife();
            resetBall();
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        // Vẽ nền
        drawBackground(g2);

        // Paddle & Ball
        paddle.draw(g2);
        ball.draw(g2);

        // Bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g2);
            }
        }

        // HUD
        HUD.render(g2, scoreSystem);
    }

    // ==== PRIVATE HELPERS ====================================================

    private void resetBall() {
        ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }
}
