package ui.panel;

import core.ResourceLoader;
import game.GameWorld;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final GameWorld world;

    public GamePanel(GameWorld world) {
        this.world = world;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        world.render(g2);
    }
}
