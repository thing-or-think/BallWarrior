package entity;

import core.Constants;
import utils.Vector2D;

import java.awt.*;

public class Ball extends Entity{

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(Constants.BALL_SPEED, Constants.BALL_SPEED);
    }

    @Override
    public void update() {
        position.add(velocity);

        if (position.x <= 0 || position.x + width >= Constants.WIDTH) {
            velocity.x *= -1;
        }

        if (position.y <= 0) {
            velocity.y *= -1;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) position.x, (int) position.y, width, height);
    }

    public void bounceY() {
        velocity.y *= -1;
    }

    public void bounceX() {
        velocity.x *= -1;
    }
}
