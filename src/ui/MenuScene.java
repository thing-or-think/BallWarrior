package ui;

import core.Constants;
import core.InputHandler;
import core.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;



public class MenuScene extends JPanel  {
    private final List<Button> buttons = new ArrayList<>();
    private final InputHandler input;

    private final Runnable onPlay;
    private final Runnable onShop;
    private final Runnable onInventory;
    private final Runnable onQuit;

    private ImageIcon backgroundGif;

    //Constructor
    public MenuScene(InputHandler input, Runnable onPlay, Runnable onShop,Runnable onInventory, Runnable onQuit) {
        this.input = input;
        this.onPlay = onPlay;
        this.onShop = onShop;
        this.onInventory = onInventory;
        this.onQuit = onQuit;

        setBackground(Color.BLACK);
        this.backgroundGif = new ImageIcon("BallWarrior-master/assets/images/background2.gif");

        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        String[] texts = {"PLAY", "SHOP", "INVENTORY", "QUIT"};
        int startY = 350;
        int spacing = 50;

        for (int i = 0; i < texts.length; i++) {
            buttons.add(new Button(texts[i], Constants.WIDTH / 2, startY + i * spacing, fm));
        }
        //mouse
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());

        new javax.swing.Timer(16, e -> repaint()).start();
    }

    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.hovered = button.contains(mx,my);
            if(button.hovered && input.consumeClick()) {
                System.out.println("Clicked"+button.text);
                switch (button.text) {
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
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 32));

        if (backgroundGif != null) {
            g2.drawImage(backgroundGif.getImage(), 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        }

        update();

        for (Button button : buttons) {
            button.draw(g2);
        }
    }

}
