package entity;

import core.Constants;
import utils.Vector2D;

import java.awt.*;

public class Ball extends Entity{
    private boolean stuck = true; // mặc định dính paddle

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(0, 0);
        this.previousPosition = new Vector2D(x, y);
    }

    @Override
    public void update() {
        if (stuck) return; // nếu còn dính paddle thì không di chuyển

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


    public boolean isStuck() {
        return stuck;
    }

    public void launch() {
        if (stuck) {
            stuck = false;
            this.velocity.set(Constants.BALL_SPEED, -Constants.BALL_SPEED);
        }
    }

    public void reset(float x, float y) {
        this.position.set(x, y);
        this.previousPosition.set(x, y);
        this.velocity.set(0, 0);
        this.stuck = true;
    }

}
