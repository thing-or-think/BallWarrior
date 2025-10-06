package core;

import game.GameScene;

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel là lớp trung gian giữa Swing và GameScene logic.
 * Nó chỉ đảm nhiệm vẽ frame hiện tại (double buffer).
 */
public class GamePanel extends JPanel {

    private final GameScene gameScene;

    public GamePanel(GameScene gameScene) {
        this.gameScene = gameScene;
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameScene.render(g);
    }
}
