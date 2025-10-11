package entity;

import utils.Constants;
import core.ResourceLoader;
import utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Ball extends Entity{
    public static BufferedImage equippedBallImage = null;
    public static Color equippedBallColor = null;
    private int radius;

    public static void loadEquippedAssets() {
        int equippedBallId = ResourceLoader.getEquippedBallId("docs/balls.txt");
        List<Skins> allBalls = ResourceLoader.loadSkins("docs/balls.txt");
        Skins equippedSkin = null;
        for (Skins skin : allBalls) {
            if (skin.getId() == equippedBallId) {
                equippedSkin = skin;
                break;
            }
        }
        if (equippedSkin != null) {
            if (equippedSkin.getImg() != null) {
                equippedBallImage = equippedSkin.getImg();
                equippedBallColor = null;
            } else {
                equippedBallColor = equippedSkin.getColor();
                equippedBallImage = null;
            }
        } else {
            equippedBallImage = ResourceLoader.loadImg("assets/images/Balls/ball.png");
            equippedBallColor = Color.RED;
        }
        System.out.println("✅ Assets Ball Equipped Loaded to static field.");
    }

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(1, -1).normalized().multiplied(Constants.BALL_SPEED);
        this.previousPosition = new Vector2D(x, y);
        this.radius = Constants.BALL_SIZE / 2;
        this.img = equippedBallImage;
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
        if (img != null) {
            g.drawImage(img,(int)position.x,(int)position.y,Constants.BALL_SIZE,Constants.BALL_SIZE,null);
        }else {
            g.setColor(equippedBallColor);
            g.fillOval((int) position.x, (int) position.y, width, height);
        }
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
