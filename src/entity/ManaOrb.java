package entity;

import utils.Constants;

import java.awt.*;

public class ManaOrb extends Entity{
    private int manaAmount;

    public ManaOrb(float x, float y, int manaAmount) {
        super(x, y, 16, 16);
        this.manaAmount = manaAmount;
    }

    @Override
    public void update() {
        position.y += 1;
        if (position.y > Constants.HEIGHT) {
            this.setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillOval((int)position.x, (int)position.y, width, height);
    }

    public int getManaAmount() {
        return manaAmount;
    }
}
