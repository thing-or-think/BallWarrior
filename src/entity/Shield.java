package entity;

import java.awt.*;
import utils.Constants;

public class Shield extends Entity {
    private float timer;
    private float duration;

    public Shield(float x, float y, int width, int height, float duration) {
        super(x, y, width, height);
        this.duration = duration;
        this.timer = 0f;
    }

    @Override
    public void setAlive(boolean isAlive) {
        super.setAlive(isAlive);
        if (isAlive) {
            this.timer = duration;
        }
    }

    public void update(float deltaTime) {
        if (isAlive()) {
            timer -= deltaTime;
            if (timer <= 0f) {
                setAlive(false);
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isAlive) {
            g.setColor(Color.CYAN);
            g.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        }
    }
}
