package entity;

import utils.Constants;
import core.ResourceLoader;
import utils.Vector2D;

import java.awt.*;

public class Ball extends Entity{
    private int radius;
    private boolean stuck = true; // mặc định dính paddle
    private boolean isFireBall = false; //mặc định bóng thường

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(1, -1).normalized().multiplied(Constants.BALL_SPEED);
        this.previousPosition = new Vector2D(x, y);
        this.radius = Constants.BALL_SIZE / 2;
        this.img = ResourceLoader.loadImg("BallWarrior-master/assets/images/ball.png");
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
    public void draw(Graphics2D g) {
        if (img != null) {
            g.drawImage(img,(int)position.x,(int)position.y,Constants.BALL_SIZE,Constants.BALL_SIZE,null);
        }else {
            g.setColor(Color.WHITE);
            g.fillOval((int) position.x, (int) position.y, width, height);
        }
    }

    public void bounceY() {
        velocity.y *= -1;
    }

    public void bounceX() {
        velocity.x *= -1;
    }

    //phóng bóng khi nóng đang ở paddle
    public void launch() {
        if (stuck) {
            stuck = false;
            this.velocity.set(Constants.BALL_SPEED, -Constants.BALL_SPEED);
        }
    }

    //reset bóng
    public void reset(float x, float y) {
        this.position.set(x, y);
        this.previousPosition.set(x, y);
        this.velocity.set(0, 0);
        this.stuck = true;
    }

    public Vector2D getCenter() {
        return new Vector2D(position.x + width / 2, position.y + height / 2);
    }

    public boolean isFireBall() {
        return isFireBall;
    }

    public void setFireBall(boolean val) {
        isFireBall = val;
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    public int getRadius() { return radius; }
}
