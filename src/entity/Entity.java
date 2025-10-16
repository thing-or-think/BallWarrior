package entity;

import utils.Constants;
import utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    // 1. Fields
    protected Vector2D position;
    protected Vector2D previousPosition;
    protected Vector2D velocity;
    protected int width;
    protected int height;
    protected BufferedImage img;
    protected Color color;

    // 2. Constructor
    public Entity(float x, float y, int width, int height) {
        this.position = new Vector2D(x, y);
        this.previousPosition = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.width = width;
        this.height = height;
    }

    // 3. Public methods chính
    public void update() {
        previousPosition.set(position.x, position.y);
        position.add(velocity);
        clampPosition();
    }

    public abstract void draw(Graphics2D g);

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        clampPosition();
    }

    public void setPosition(Vector2D other) {
        setPosition(other.x, other.y);
    }

    public void setPreviousPosition(float x, float y) {
        previousPosition.x = x;
        previousPosition.y = y;
    }

    public void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    public void setVelocity(Vector2D other) {
        setVelocity(other.x, other.y);
    }

    // 4. Protected/private helper methods
    protected void clampPosition() {
        if (position.x < 0) position.x = 0;
        if (position.x + width > Constants.WIDTH) position.x = Constants.WIDTH - width;
        if (position.y < 0) position.y = 0;
        if (position.y + height > Constants.HEIGHT) position.y = Constants.HEIGHT - height;
    }

    // 5. Getters
    public float getX() { return position.x; }

    public float getY() { return position.y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Vector2D getVelocity() { return velocity; }

    public Vector2D getPosition() { return new Vector2D(position.x, position.y); }

    public Vector2D getPreviousPosition() { return previousPosition; }

    public Vector2D getCenter() {
        return new Vector2D(position.x + width / 2f, position.y + height / 2f);
    }
    //6. Setters
    public void setImg (BufferedImage i) {this.img = i; }

    public void setColor(Color color) {
        this.color = color;
    }
}
