package core;

import game.GameScene;
import ui.MenuScene;
import ui.ShopScene;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final GameScene gameScene;
    private final MenuScene menuScene;

    // Panel chứa game
    private final JPanel gamePanel;

    // Thread game loop
    private Thread gameThread;
    private boolean running = false;

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo game scene
        this.gameScene = new GameScene(input);

        // Panel vẽ game
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

        // Tạo menu scene
        this.menuScene = new MenuScene(
                input,
                this::startGame,
                this::openShop,
                () -> System.out.println("Inventory!"),
                () -> System.exit(0)
        );
        menuScene.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
    }

    /**
     * Khởi động game
     */
    private void startGame() {
        input.resetMouse();

        frame.setContentPane(gamePanel);  // Chuyển sang màn game
        frame.revalidate();
        frame.repaint();

        gamePanel.requestFocusInWindow();

        running = true;
        gameThread = new Thread(this::runGameLoop);
        gameThread.start();
    }

    /**
     * Vòng lặp game
     */
    private void runGameLoop() {
        final int FPS = 60;
        final double TIME_PER_TICK = 1e9 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                gameScene.update();
                gamePanel.repaint();
                delta--;
            }
        }
    }

    /**
     * Mở shop
     */
    private void openShop() {
        input.resetMouse();
        ShopScene shopScene = new ShopScene(input, this::backToMenu);
        frame.setContentPane(shopScene);
        frame.revalidate();
    }

    /**
     * Quay về menu
     */
    private void backToMenu() {
        input.resetMouse();
        frame.setContentPane(menuScene);
        frame.revalidate();
    }

    /**
     * Cho Main gọi để mở menu lần đầu
     */
    public JPanel getMenuScene() {
        return menuScene;
    }
}