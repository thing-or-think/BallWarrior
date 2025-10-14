package ui.button;

import ui.base.Button;
import ui.base.ButtonGroup;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IconButton extends Button {
    private ButtonGroup buttonGroup;

    public IconButton(String text,BufferedImage icon, int x, int y, int width, int height, Runnable activity) {
        super(text, icon, x, y, width, height);
        this.activity = activity;
    }

    @Override
    public void draw(Graphics2D g) {
        if (clicked) {
            g.fillRect(bound.x, bound.y, bound.width, bound.height);
        }
        if (icon != null) {
            g.drawImage(icon, bound.x, bound.y, bound.width, bound.height, null);
        }
    }

    @Override
    public void onClick() {
        if (buttonGroup != null) {
            buttonGroup.select(this);
        } else {
            clicked = true; // nếu không có group thì toggle bình thường
        }
        if (activity != null)
            activity.run();
    }

    public void setButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }
}
