package core;

import game.GameScene;

public class GameLoop implements Runnable {

    private final GameScene gameScene;
    private Thread loopThread;
    private volatile boolean running = false;
    private final int TARGET_FPS = 60;

    public GameLoop(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void start() {
        if (running) return;
        running = true;
        loopThread = new Thread(this, "GameLoop");
        loopThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (loopThread != null) loopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final double frameTime = 1_000_000_000.0 / TARGET_FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / frameTime;
            lastTime = now;

            while (delta >= 1) {
                gameScene.update();
                delta--;
            }

            // Gọi repaint từ EDT
            javax.swing.SwingUtilities.invokeLater(() -> {
                java.awt.Component c = gameScene.getClass().getEnclosingClass() != null
                        ? null : null; // không cần, render đã qua GamePanel
            });

            try {
                Thread.sleep(1000 / TARGET_FPS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
