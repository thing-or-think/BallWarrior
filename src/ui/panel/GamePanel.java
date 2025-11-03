package ui.panel;

import core.ResourceLoader;
import game.GameWorld;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final GameWorld world;
    private BufferedImage background;

    public GamePanel(GameWorld world) {
        this.world = world;
        background = ResourceLoader.loadImage("assets/images/Bg/scene3.png"); // scene 2/3 theo level
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(background,0,0, Constants.GAME_PANEL_WIDTH,Constants.GAME_PANEL_HEIGHT,null);
        world.render(g2);
    }
}
