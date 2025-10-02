import javax.swing.*;
import core.GameEngine;
import core.ResourceLoader;

public class Main {
    public static void main(String[] args) {
         //🔊 phát nhạc nền
        //ResourceLoader.playBackgroundMusic("sounds/bossbattle.wav");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BallWarrior");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GameEngine engine = new GameEngine();
            frame.add(engine);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            engine.start();
        });
    }
}
