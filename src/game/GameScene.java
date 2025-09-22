package game;

import core.Constants;
import core.InputHandler;
import entity.Ball;
import entity.Entity;
import entity.Paddle;

import java.awt.*;

public class GameScene {

    private Paddle paddle;
    private Ball ball;
    private InputHandler input;

    public GameScene(InputHandler input) {
        this.input = input;
        init();
    }

    private void init() {
        paddle = new Paddle(Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                            Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                                input);

        ball = new Ball(paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE);
    }

    public void update() {
        paddle.update();
        ball.update();

        if (checkCollision(paddle, ball)) {
            ball.bounceY();
        }
    }

    public void render(Graphics g) {
        paddle.draw(g);
        ball.draw(g);
    }

    private boolean checkCollision(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
