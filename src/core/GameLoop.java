package core;

import game.GameScene;

public class GameLoop implements Runnable {
    private GameScene gameScene;
    private final SceneManager sceneManager;

    private Thread loopThread;
    private volatile boolean running = false;

    public GameLoop(GameScene gameScene, SceneManager sceneManager) {
        this.gameScene = gameScene;
        this.sceneManager = sceneManager;
    }

    public void setGameScene(GameScene gameScene) { // Thêm setter này
        this.gameScene = gameScene;
    }

    public boolean isRunning() { // Thêm phương thức này
        return running;
    }

    public void start() {
        if (running) return; // Nếu đã chạy thì không làm gì
        running = true;
        loopThread = new Thread(this);
        loopThread.start();
        System.out.println("GameLoop started."); // Debug
    }

    public void stop() {
        if (!running) return; // Nếu chưa chạy thì không làm gì
        running = false; // Đặt biến cờ để dừng vòng lặp
        if (loopThread != null) {
            try {
                loopThread.join(200); // Chờ luồng kết thúc tối đa 200ms
            } catch (InterruptedException e) {
                System.err.println("GameLoop thread interrupted while stopping: " + e.getMessage());
                Thread.currentThread().interrupt(); // Đặt lại cờ ngắt
            }
            loopThread = null; // Xóa tham chiếu đến luồng đã dừng
        }
        System.out.println("GameLoop stopped."); // Debug
    }

    @Override
    public void run() {
        final int FPS = 60;
        final double TIME_PER_TICK = 1e9 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) { // Vòng lặp sẽ chạy cho đến khi running = false
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                // Đảm bảo gameScene không null trước khi update
                if (gameScene != null) {
                    gameScene.update();
                }
                sceneManager.repaintGame();
                delta--;
            }
        }
        System.out.println("GameLoop stopped."); // Thêm debug log
    }
}
