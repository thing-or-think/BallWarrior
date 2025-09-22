package core;

import javax.swing.*;
import java.awt.*;

public class GameEngine extends JPanel implements Runnable{
    private Thread gameThread;
    private boolean running = false;

    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    public GameEngine() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        setFocusable(true);
//        requestFocus();
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
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
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
