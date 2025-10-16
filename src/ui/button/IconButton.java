package ui.button;

import ui.base.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IconButton extends Button {
    public IconButton(String text,BufferedImage icon, int x, int y, int width, int height, Runnable activity) {
        super(text, icon, x, y, width, height);
        this.activity = activity;
    }

    @Override
    public void draw(Graphics2D g) {
        if (icon != null) {
            g.drawImage(icon, bound.x, bound.y, bound.width, bound.height, null);
        }
    }

    @Override
    public void onClick() {
        if (activity != null)
            activity.run();
    }
}
