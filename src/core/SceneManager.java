package core;

import ui.base.Scene;

import javax.swing.*;
import java.awt.*;

/**
 * SceneManager quản lý việc chuyển đổi giữa các Scene giao diện.
 */
public class SceneManager extends JPanel {

    private Scene currentScene;

    public SceneManager(Scene initialScene) {
        setLayout(new BorderLayout());
        if (initialScene != null) {
            setScene(initialScene);
        }
    }

    /** Chuyển sang Scene mới */
    public void setScene(Scene newScene) {
        if (newScene == null) {
            System.err.println("[SceneManager] ⚠️ newScene == null");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            removeAll();
            currentScene = newScene;
            add(currentScene, BorderLayout.CENTER);
            revalidate();
            repaint();
            currentScene.requestFocusInWindow();
        });
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}
