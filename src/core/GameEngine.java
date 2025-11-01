package core;
import javax.swing.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final SceneManager sceneManager; // Đảm bảo có dòng này

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo scene manager
        this.sceneManager = new SceneManager(frame, input); // Đảm bảo có dòng này
    }

    public JPanel getScene() {
        return sceneManager.getCurrentScene();
    }
}
