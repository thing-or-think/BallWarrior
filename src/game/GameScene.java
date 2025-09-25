package game;

import core.Constants;
import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameScene {

    private Paddle paddle;
    private Ball ball;
    private InputHandler input;
    private List<Brick> bricks;

    private int score;
    private int lives = 3;

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

        // Bóng ngay trên paddle
        resetBall();

        // Gạch demo
        bricks = new ArrayList<>();
        int brickWidth = 60;
        int brickHeight = 20;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                bricks.add(new Brick(
                        50 + col * (brickWidth + 5),
                        50 + row * (brickHeight + 5),
                        brickWidth,
                        brickHeight,
                        1,
                        Color.RED
                ));
            }
        }
    }

    private void resetBall() {
        ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE
        );
    }

    public void update() {
        paddle.update();
        ball.update();

        // Bóng - paddle
        CollisionSystem.handleBallCollision(ball, paddle, true);
        CollisionSystem.handleBallInsideEntity(ball, paddle);

        // Bóng - gạch
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (!brick.isDestroyed()) {
                if (CollisionSystem.handleBallCollision(ball, brick, false)) {
                    brick.hit();
                    if (brick.isDestroyed()) {
                        score += 100;
                    }
                }
            }
        }

        // Nếu bóng rơi xuống dưới màn hình
        if (ball.getY() > Constants.HEIGHT) {
            lives--;
            resetBall();
        }
    }

    public void render(Graphics g) {
        // Vẽ paddle + ball
        paddle.draw(g);
        ball.draw(g);

        // Vẽ bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g);
            }
        }

        // HUD cơ bản
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, Constants.WIDTH - 80, 20);
    }
}
