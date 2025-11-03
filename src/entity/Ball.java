package entity;

import data.SkinData;
import utils.Constants;
import core.ResourceLoader;
import utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Ball extends Entity {
    public static BufferedImage equippedBallImage = null;
    public static Color equippedBallColor = null;
    private int radius;
    private boolean stuck = true;
    private static SkinData skinData;
    private boolean isFireBall = false; //mặc định bóng thường

    public Ball(float x, float y) {
        super(x, y, Constants.BALL_SIZE, Constants.BALL_SIZE);
        this.velocity = new Vector2D(0, -1).normalized().multiplied(Constants.BALL_SPEED);
        this.previousPosition = new Vector2D(x, y);
        this.radius = Constants.BALL_SIZE / 2;
        this.img = equippedBallImage;
    }

    public static void setSkin(SkinData skinData) {
        if (skinData.equals(Ball.skinData)) {
            return;
        } else {
            Ball.skinData = skinData;
            loadDisplay();
        }
    }

    private static void loadDisplay() {
        if (skinData == null || skinData.getDisplay() == null) return;

        String type = skinData.getDisplay().getType();
        String value = skinData.getDisplay().getValue();

        if ("color".equalsIgnoreCase(type)) {
            try {
                equippedBallColor = Color.decode(value);
                if (equippedBallColor == null) {
                    equippedBallColor = Color.WHITE;
                }
            } catch (Exception e) {
                equippedBallColor = Color.WHITE;
            }
        } else if ("image".equalsIgnoreCase(type)) {
            equippedBallImage = ResourceLoader.loadImage(value);
        }
    }

    @Override
    public void update() {
        if (stuck) return;
        previousPosition.set(position.x, position.y);
        position.add(velocity);
        if (position.y + width > Constants.GAME_PANEL_HEIGHT) {
            this.setAlive(false);
        }
        clampPosition();
    }

    @Override
    public void clampPosition() {
        if (position.x <= 0) {
            position.x = 0;
            velocity.x = Math.abs(velocity.x);
        } else if (position.x + width >= Constants.GAME_PANEL_WIDTH) {
            position.x = Constants.GAME_PANEL_WIDTH - width;
            velocity.x = -Math.abs(velocity.x);
        }
        if (position.y <= 0) {
            position.y = 0;
            velocity.y = Math.abs(velocity.y);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        String type = skinData.getDisplay().getType();

        if ("color".equalsIgnoreCase(type)) {
            g.setColor(equippedBallColor);
            g.fillOval((int) position.x, (int) position.y, width, height);
            g.setColor(Color.WHITE);
            g.drawOval((int) position.x, (int) position.y, width, height);
        } else {
            g.drawImage(img, (int) position.x, (int) position.y, Constants.BALL_SIZE, Constants.BALL_SIZE, null);
        }
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

    public Vector2D getCenter() {
        return new Vector2D(position.x + width / 2, position.y + height / 2);
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    public int getRadius() {
        return radius;
    }

    public void setFireBall(boolean isfireBall) {
        this.isFireBall = isfireBall;
    }

    public boolean isFireBall() {
        return isFireBall;
    }
}
