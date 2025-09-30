package entity;

import core.Constants;
import utils.Vector2D;

import java.awt.*;

public class Ball extends Entity{
    private int radius;

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(1, -1).normalized().multiplied(Constants.BALL_SPEED);
        this.previousPosition = new Vector2D(x, y);
        this.radius = Constants.BALL_SIZE / 2;
    }

    @Override
    public void update() {
        previousPosition.set(position.x, position.y);
        position.add(velocity);

        clampPosition();
    }

    @Override
    public void clampPosition() {
        // Cạnh trái và phải
        if (position.x <= 0) {
            position.x = 0; // đảm bảo không vượt ra ngoài
            velocity.x = Math.abs(velocity.x);
        } else if (position.x + width >= Constants.WIDTH) {
            position.x = Constants.WIDTH - width;
            velocity.x = -Math.abs(velocity.x);
        }

        // Cạnh trên
        if (position.y <= 0) {
            position.y = 0;
            velocity.y = Math.abs(velocity.y);
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

    public Vector2D getCenter() {
        return new Vector2D(position.x + width / 2, position.y + height / 2);
    }

    public int getRadius() { return radius; }
}
