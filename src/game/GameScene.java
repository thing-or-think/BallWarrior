package game;

import core.Constants;
import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import ui.HUD;
import game.collision.CollisionSystem;
import game.collision.CollisionResult;

import java.awt.*;
import java.util.List;

public class GameScene {

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;

    public GameScene(InputHandler input) {
        init(input);
    }

    /**
     * Khởi tạo màn chơi:
     * - Paddle ở giữa đáy
     * - Bóng trên paddle
     * - Reset điểm/mạng
     * - Load bricks từ file level
     */
    private void init(InputHandler input) {
        paddle = new Paddle(
                Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                input
        );
        resetBall();
        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        bricks = levelManager.load("assets/levels/level1.txt");

        // Khởi tạo CollisionSystem và đăng ký entity
        collisionSystem = new CollisionSystem();
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }
    }

    /**
     * Đặt bóng trở lại trên paddle (dùng khi mất mạng).
     */
    private void resetBall() {
        ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
    }

    /**
     * Cập nhật logic game mỗi frame:
     * - Paddle, Ball
     * - Va chạm ball với colliders
     * - Mất mạng khi bóng rơi xuống đáy
     */
    public void update() {
        paddle.update();
        ball.update();

        // Tìm va chạm gần nhất
        CollisionResult result = collisionSystem.findNearestCollision(ball);
        if (result != null) {
            if (collisionSystem.resolveCollision(ball, result)) {
                if (result.getEntity() instanceof Brick brick) {
                    if (brick.isDestroyed()) {
                        scoreSystem.addScore(100);
                        collisionSystem.unregister(brick);
                    }
                }
            }
        }

        // Mất mạng nếu bóng rơi khỏi màn
        if (ball.getY() > Constants.HEIGHT) {
            scoreSystem.loseLife();
            resetBall();
        }
    }

    /**
     * Vẽ toàn bộ scene:
     * - Paddle, Ball, Bricks
     * - HUD (score + lives)
     */
    public void render(Graphics g) {
        paddle.draw(g);
        ball.draw(g);

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g);
            }
        }

        HUD.render(g, scoreSystem);
    }

    /**
     * Kiểm tra game over (hết mạng).
     */
    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }
}
