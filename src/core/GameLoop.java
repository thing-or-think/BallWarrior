package core;

import game.GameScene;

public class GameLoop implements Runnable {
    private final GameScene gameScene;
    private final SceneManager sceneManager;

    private Thread loopThread;
    private boolean running = false;

    public GameLoop(GameScene gameScene, SceneManager sceneManager) {
        this.gameScene = gameScene;
        this.sceneManager = sceneManager;
    }

    public void start() {
        if (running) return;
        running = true;
        loopThread = new Thread(this);
        loopThread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final double TIME_PER_TICK = 1e9 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                // Calculate deltaTime in seconds before passing it
                float deltaTime = (float) (delta * TIME_PER_TICK / 1e9);
                gameScene.update(deltaTime);
                sceneManager.repaintGame();
                delta--;
            }
        }
    }
}
