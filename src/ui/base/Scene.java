package ui.base;

import core.InputHandler;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public abstract class Scene extends JPanel {

    protected final InputHandler input;
    protected ImageIcon background;
    protected final String name;
    private Timer repaintTimer;
    protected float deltaTime;

    public Scene(String name, InputHandler input) {
        this.name = name;
        this.input = input;
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setDoubleBuffered(true);
        setFocusable(true);
        setBackground(Color.BLACK);

        initInput();
    }

    protected abstract void initUI();

    protected abstract void update();

    protected abstract void render(Graphics2D g2);


    protected void initInput() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
        addMouseWheelListener(input.createMouseAdapter());
        addKeyListener(input);
    }

    public void startRepaintLoop() {
        if (repaintTimer != null) repaintTimer.stop();

        final long[] lastTime = {System.nanoTime()};

        repaintTimer = new Timer(1000 / 60, e -> {
            long currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime[0]) / 1_000_000_000f; // giây
            lastTime[0] = currentTime;

            update(); // dùng deltaTime trong update()
            repaint();
            input.update();
        });
        repaintTimer.start();

        setFocusable(true);
        requestFocusInWindow();
    }

    public void stopRepaintLoop() {
        if (repaintTimer != null) repaintTimer.stop();
    }

    protected void drawBackground(Graphics2D g2) {
        if (background != null) {
            g2.drawImage(background.getImage(), 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        render(g2);
    }

    public String getName() {
        return name;
    }
}
