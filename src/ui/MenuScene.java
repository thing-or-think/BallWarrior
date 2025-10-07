package ui;

import utils.Constants;
import core.InputHandler;
import ui.base.Button;
import ui.button.TextButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MenuScene extends JPanel {
    private final List<Button> buttons = new ArrayList<>();
    private final InputHandler input;

    private final Runnable onPlay;
    private final Runnable onShop;
    private final Runnable onInventory;
    private final Runnable onQuit;

    private ImageIcon backgroundGif;

    // Constructor
    public MenuScene(InputHandler input, Runnable onPlay, Runnable onShop, Runnable onInventory, Runnable onQuit) {
        this.input = input;
        this.onPlay = onPlay;
        this.onShop = onShop;
        this.onInventory = onInventory;
        this.onQuit = onQuit;

        initUI();
        initButtons();
        initMouse();
        startRepaintTimer();
    }

    /** Khởi tạo UI cơ bản */
    private void initUI() {
        setBackground(Color.BLACK);
        this.backgroundGif = new ImageIcon("BallWarrior-master/assets/images/background2.gif");
    }

    /** Tạo các button */
    private void initButtons() {
        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        String[] texts = {"PLAY", "SHOP", "INVENTORY", "QUIT"};
        int startY = 350;
        int spacing = 50;

        for (int i = 0; i < texts.length; i++) {
            buttons.add(new TextButton(texts[i], Constants.WIDTH / 2, startY + i * spacing, fm));
        }
    }

    /** Gắn mouse input */
    private void initMouse() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
    }

    /** Khởi động timer repaint */
    private void startRepaintTimer() {
        new javax.swing.Timer(16, e -> repaint()).start();
    }

    /** Cập nhật trạng thái menu */
    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                handleButtonClick(button.getText());
            }
        }
    }

    /** Xử lý sự kiện click button */
    private void handleButtonClick(String text) {
        System.out.println("Clicked " + text);
        switch (text) {
            case "PLAY":
                onPlay.run();
                break;
            case "SHOP":
                onShop.run();
                break;
            case "INVENTORY":
                onInventory.run();
                break;
            case "QUIT":
                onQuit.run(); // dùng callback thay vì System.exit
                break;
            default:
                break;
        }
    }

    /** Vẽ background */
    private void drawBackground(Graphics2D g2) {
        if (backgroundGif != null) {
            g2.drawImage(backgroundGif.getImage(), 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        }
    }

    /** Vẽ button */
    private void drawButtons(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 32));

        update();
        drawBackground(g2);
        drawButtons(g2);
    }
}
