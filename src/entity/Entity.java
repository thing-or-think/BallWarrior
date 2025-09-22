package entity;

import core.Constants;
import utils.Vector2D;

import java.awt.*;

public abstract class Entity {
    protected Vector2D position;
    protected Vector2D velocity;
    protected int width;
    protected int height;

    public Entity(float x, float y, int width, int height) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.width = width;
        this.height = height;
    }

    public void update() {
        position.add(velocity);
        clampPosition();
    }

    protected void clampPosition() {
        if (position.x < 0) position.x = 0;
        if (position.x + width > Constants.WIDTH)   position.x = Constants.WIDTH - width;
        if (position.y < 0) position.y = 0;
        if (position.y + height > Constants.HEIGHT) position.y = Constants.HEIGHT - height;
    }

    public abstract void draw(Graphics g);

    public float getX() {   return position.x;  }
    public float getY() {   return position.y;  }
    public int getWidth() { return width;   }
    public int getHeight(){ return height;  }
}
