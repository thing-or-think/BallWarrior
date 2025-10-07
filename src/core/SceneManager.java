package core;

import game.GameScene;
import ui.MenuScene;
import ui.ShopScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class SceneManager {
    private final JFrame frame;
    private final InputHandler input;

    private GameScene gameScene;
    private MenuScene menuScene;
    private JPanel gamePanel;

    public SceneManager(JFrame frame, InputHandler input) {
        this.frame = frame;
        this.input = input;
    }

    public void setGameScene(GameScene gameScene) {
        this.gameScene = gameScene;

        this.gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.PINK);
                g.fillRect(0, 0, getWidth(), getHeight());
                gameScene.render(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(input);
    }

    public void setMenuScene(MenuScene menuScene) {
        this.menuScene = menuScene;
    }

    public MenuScene getMenuScene() {
        return menuScene;
    }

    public void showMenuScene() {
        frame.setContentPane(menuScene);
        frame.revalidate();
        frame.repaint();
    }

    public void showGameScene() {
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    public void showShopScene(ShopScene shopScene) {
        frame.setContentPane(shopScene);
        frame.revalidate();
        frame.repaint();
    }

    public void repaintGame() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
}