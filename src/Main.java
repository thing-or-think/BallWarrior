import javax.swing.*;

import core.GameEngine;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BallWarrior");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GameEngine engine = new GameEngine(frame);
            frame.setContentPane(engine.getMenuScene());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
