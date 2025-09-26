package entity;

import core.Constants;
import core.InputHandler;

import java.awt.*;

public class Paddle extends Entity {

    private final InputHandler input;

    public Paddle(float x, float y, InputHandler input) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        this.input = input;
    }

    @Override
    public void clampPosition() {
        if (position.x < 0) position.x = 0;
        if (position.x + width > Constants.WIDTH) position.x = Constants.WIDTH - width;
    }


    @Override
    public void update() {
        velocity.x = 0;

        if (input.isLeftPressed()) {
            velocity.x = -Constants.PADDLE_SPEED;
        } else if (input.isRightPressed()) {
            velocity.x = Constants.PADDLE_SPEED;
        }

        previousPosition.set(position.x, position.y);
        position.add(velocity);

        clampPosition();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int) position.x, (int) position.y, width, height);
    }
}
