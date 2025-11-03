package ui.scene;

import ui.base.Button;
import ui.base.Scene;
import ui.button.MenuButton;
import utils.Constants;
import core.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuScene extends Scene {

    private final List<Button> buttons = new ArrayList<>();

    public MenuScene(InputHandler input, String currentPlayerName, Runnable onPlay, Runnable onShop, Runnable onChangePlayer, Runnable onInventory, Runnable onQuit) {
        super("Menu", input);

        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        String playText = "PLAY AS " + (currentPlayerName != null ? currentPlayerName.toUpperCase() : "PLAYER");

        // Thêm "INVENTORY" vào danh sách
        String[] texts = {playText, "CHANGE PLAYER", "SHOP", "INSTRUCTION", "QUIT"};
        Runnable[] runnables = {onPlay, onChangePlayer, onShop, onInventory, onQuit};

        int startY = 380;
        int spacing = 50; // Giảm khoảng cách một chút để vừa 5 nút

        for (int i = 0; i < texts.length; i++) {
            buttons.add(new MenuButton(texts[i], Constants.WINDOW_WIDTH / 2, startY + i * spacing, fm, runnables[i]));
        }

        initUI();
    }

    // ==== IMPLEMENT ABSTRACT METHODS ========================================

    @Override
    protected void initUI() {
        background = new ImageIcon("assets/images/Bg/background2.gif");
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                button.onClick();
            }
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

}
