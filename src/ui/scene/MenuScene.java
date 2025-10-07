package ui.scene;

import ui.base.Button;
import ui.base.Scene;
import ui.button.TextButton;
import utils.Constants;
import core.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuScene extends Scene {

    private final List<Button> buttons = new ArrayList<>();
    private final Runnable onPlay;
    private final Runnable onShop;
    private final Runnable onInventory;
    private final Runnable onQuit;

    // ==== CONSTRUCTOR ========================================================

    public MenuScene(InputHandler input, Runnable onPlay, Runnable onShop, Runnable onInventory, Runnable onQuit) {
        super("Menu", input);
        this.onPlay = onPlay;
        this.onShop = onShop;
        this.onInventory = onInventory;
        this.onQuit = onQuit;

        initUI();

    }

    // ==== IMPLEMENT ABSTRACT METHODS ========================================

    @Override
    protected void initUI() {
        background = new ImageIcon("BallWarrior-master/assets/images/background2.gif");

        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        String[] texts = {"PLAY", "SHOP", "INVENTORY", "QUIT"};
        int startY = 350;
        int spacing = 50;

        for (int i = 0; i < texts.length; i++) {
            buttons.add(new TextButton(texts[i], Constants.WIDTH / 2, startY + i * spacing, fm));
        }
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                handleButtonClick(button.getText());
            }
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    // ==== PRIVATE UTIL ======================================================

    private void handleButtonClick(String text) {
        System.out.println("Clicked " + text);
        switch (text) {
            case "PLAY" -> onPlay.run();
            case "SHOP" -> onShop.run();
            case "INVENTORY" -> onInventory.run();
            case "QUIT" -> onQuit.run();
        }
    }
}
