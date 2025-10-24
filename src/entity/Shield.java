package entity;

import java.awt.*;
import utils.Constants;

public class Shield extends Entity {
    public Shield(float x, float y, int width, int height, float duration) {
        super(x, y, width, height);
        setAlive(false);
    }

    @Override
    public void draw(Graphics2D g) {
        if (isAlive) {
            g.setColor(Color.CYAN);
            g.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        }
    }
}
