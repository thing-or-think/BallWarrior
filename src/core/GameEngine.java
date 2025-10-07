package core;

import game.GameScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final SceneManager sceneManager;

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo scene manager
        this.sceneManager = new SceneManager(frame, input);

    }

    /** Bắt đầu game */
    private void startGame() {
        input.resetMouse();
    }


    /** Cho Main gọi để mở menu lần đầu */
    public JPanel getScene() {
        return sceneManager.getCurrentScene();
    }
}