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
    private float combo = 1f;
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

        // Khởi tạo bricks hình vuông
        bricks = new ArrayList<>();
        int brickSize = 40; // gạch vuông
        int rows = 5;
        int cols = 10;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Brick.Type type;
                // Ví dụ: chọn loại theo cột
                if (col % 4 == 0) type = Brick.Type.BEDROCK;
                else if (col % 4 == 1) type = Brick.Type.COBBLESTONE;
                else if (col % 4 == 2) type = Brick.Type.GOLD;
                else type = Brick.Type.DIAMOND;

                bricks.add(new Brick(
                        50 + col * (brickSize + 5),
                        50 + row * (brickSize + 5),
                        brickSize,
                        brickSize,
                        type
                ));
            }
        }
    }

    private void resetBall() {
        if (ball == null) {
            ball = new Ball(
                    paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                    paddle.getY() - Constants.BALL_SIZE
            );
        } else {
            ball.reset(
                    paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                    paddle.getY() - Constants.BALL_SIZE
            );
        }

        combo = 1f; // reset combo khi mất mạng
    }

    public void update() {
        paddle.update();

        // Nếu bóng đang dính paddle → cập nhật theo paddle
        if (ball.isStuck()) {
            ball.reset(
                    paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                    paddle.getY() - Constants.BALL_SIZE
            );

            // nếu người chơi nhấn Space → thả bóng
            if (input.isSpacePressed()) {
                ball.launch();
            }
        } else {
            ball.update();
        }

        // Bóng - paddle
        if (CollisionSystem.handleBallCollision(ball, paddle, true)) {
            combo = 1f; // reset combo khi bóng chạm paddle
        }
        CollisionSystem.handleBallInsideEntity(ball, paddle);

        // Bóng - gạch
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (!brick.isDestroyed()) {
                if (CollisionSystem.handleBallCollision(ball, brick, false)) {
                    brick.hit();
                    if (brick.isDestroyed()) {
                        score += (int)(brick.getScoreValue() * combo);
                        combo = Math.min(combo + 0.5f, 3f); // tăng combo nhưng tối đa 3
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
        g.drawString("x" + combo, 10, 40);
    }
}
