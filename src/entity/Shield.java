package entity;

import java.awt.*;
import utils.Constants;

public class Shield extends Entity {
    private boolean active;
    private float timer;
    private float duration;

    public Shield(float x, float y, int width, int height, float duration) {
        super(x, y, width, height);
        this.duration = duration;
        this.active = false;
        this.timer = 0f;
    }

    public void activate() {
        this.active = true;
        this.timer = duration;
    }

    public void update(float deltaTime) {
        if (active) {
            timer -= deltaTime;
            if (timer <= 0f) {
                active = false;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void draw(Graphics2D g) {
        if (active) {
            g.setColor(Color.CYAN);
            g.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        }
    }
}
