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

    // ==== CONSTRUCTOR ========================================================

    public Scene(String name, InputHandler input) {
        this.name = name;
        this.input = input;
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setDoubleBuffered(true);
        setFocusable(true);
        setBackground(Color.BLACK);

        initInput();
        startRepaintLoop();
    }

    // ==== ABSTRACT METHODS ===================================================

    protected abstract void initUI();

    protected abstract void update();

    protected abstract void render(Graphics2D g2);

    // ==== COMMON METHODS =====================================================

    protected void initInput() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
        addKeyListener(input);
    }

    public void startRepaintLoop() {
        repaintTimer = new Timer(16, e -> repaint());
        repaintTimer.start();
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

    // ==== PAINT =============================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        update();            // Cập nhật logic
        drawBackground(g2);  // Vẽ nền
        render(g2);          // Vẽ nội dung cụ thể của scene con
    }

    // ==== UTIL ===============================================================

    public String getName() {
        return name;
    }
}
