package game;

import core.Constants;
import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import ui.HUD;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameScene {

    private Paddle paddle;
    private List<Ball> balls;
    private InputHandler input;
    private List<Brick> bricks;

    private HUD hud; // quản lý score, combo, lives
    private PowerUpSystem powerUpSystem;

    public GameScene(InputHandler input) {
        this.input = input;
        init();
    }

    private void init() {
        // Paddle ở giữa đáy màn hình
        paddle = new Paddle(
                Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                input
        );

        // HUD (3 mạng)
        hud = new HUD(3);

        // Bóng ngay trên paddle
        balls = new ArrayList<>();
        resetBall();

        // Khởi tạo bricks hình vuông
        bricks = new ArrayList<>();
        int brickSize = 40; // gạch vuông
        int rows = 5;
        int cols = 10;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Brick.Type type;
                switch (col % 4) {
                    case 0: type = Brick.Type.BEDROCK; break;
                    case 1: type = Brick.Type.COBBLESTONE; break;
                    case 2: type = Brick.Type.GOLD; break;
                    default: type = Brick.Type.DIAMOND; break;
                }
                bricks.add(new Brick(
                        50 + col * (brickSize + 5),
                        50 + row * (brickSize + 5),
                        brickSize,
                        brickSize,
                        type
                ));
            }
        }

        // PowerUpSystem
        powerUpSystem = new PowerUpSystem(input, paddle, balls, bricks);

    }

    private void resetBall() {
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE
        );
        balls.clear();
        balls.add(ball);
        hud.resetCombo();
    }

    public void update(float deltaTime) {
        paddle.update();

        // Cập nhật PowerUpSystem
        powerUpSystem.update(deltaTime);

        // Cập nhật tất cả bóng
        for (Ball ball : balls) {
            if (ball.isStuck()) {
                ball.reset(
                        paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE
                );
                if (input.isSpacePressed()) {
                    ball.launch();
                }
            } else {
                ball.update();
            }
        }

        // Va chạm bóng - paddle
        for (Ball ball : balls) {
            if (CollisionSystem.handleBallCollision(ball, paddle, true)) {
                hud.resetCombo();
            }
            CollisionSystem.handleBallInsideEntity(ball, paddle);
        }

        // Va chạm bóng - shield
        Shield shield = powerUpSystem.getShield();
        if (shield != null && shield.isActive()) {
            for (Ball ball : balls) {
                if (CollisionSystem.handleBallCollision(ball, shield, false)) {
                    // hiệu ứng nếu muốn
                }
            }
        }

        // Va chạm bóng - gạch
        for (Ball ball : balls) {
            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && CollisionSystem.handleBallCollision(ball, brick, false)) {
                    brick.hit(1);
                    if (brick.isDestroyed()) {
                        hud.addScore((int)(brick.getScoreValue() * hud.getCombo()));
                        hud.increaseCombo(0.5f);
                    }
                }
            }
        }

        // Kiểm tra bóng rơi xuống dưới
        List<Ball> ballsToRemove = new ArrayList<>();
        for (Ball ball : balls) {
            if (ball.getY() > Constants.HEIGHT) {
                ballsToRemove.add(ball);
            }
        }
        for (Ball ball : ballsToRemove) {
            balls.remove(ball);
        }

        // Nếu hết bóng → mất mạng và reset
        if (balls.isEmpty()) {
            hud.loseLife();
            resetBall();
        }
    }


    public void render(Graphics g) {
        // Vẽ paddle
        paddle.draw(g);

        // Vẽ tất cả bóng
        for (Ball ball : balls) {
            ball.draw(g);
        }

        // Vẽ bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g);
            }
        }

        // Vẽ HUD
        hud.render(g, Constants.WIDTH, Constants.HEIGHT);

        // Vẽ shield
        Shield shield = powerUpSystem.getShield();
        if (shield != null && shield.isActive()) {
            shield.draw(g);
        }


    }
}
