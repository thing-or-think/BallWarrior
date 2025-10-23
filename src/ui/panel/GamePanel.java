package ui.panel;

import game.GameWorld;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameWorld world;

    public GamePanel(GameWorld world) {
        this.world = world;
        setBackground(Color.decode("#212121"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        world.render(g2);
        g2.setColor(Color.decode("#879e1a"));
        g2.setStroke(new BasicStroke(2)); // độ dày viền
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
