//package ui.scene;
//
//import ui.base.Button;
//import ui.base.Scene;
//import ui.button.MenuButton;
//import utils.Constants;
//import core.InputHandler;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MenuScene extends Scene {
//
//    private final List<Button> buttons = new ArrayList<>();
//
//    public MenuScene(InputHandler input, Runnable onPlay, Runnable onShop, Runnable onInventory, Runnable onQuit) {
//        super("Menu", input);
//
//        Font font = new Font("Serif", Font.PLAIN, 32);
//        FontMetrics fm = getFontMetrics(font);
//
//        String[] texts = {"PLAY", "SHOP", "INVENTORY", "QUIT"};
//        Runnable[] runnables = {onPlay, onShop, onInventory, onQuit};
//        int startY = 350;
//        int spacing = 50;
//
//        for (int i = 0; i < texts.length; i++) {
//            buttons.add(new MenuButton(texts[i], Constants.WIDTH / 2, startY + i * spacing, fm, runnables[i]));
//        }
//
//        initUI();
//
//    }
//
//    // ==== IMPLEMENT ABSTRACT METHODS ========================================
//
//    @Override
//    protected void initUI() {
//        background = new ImageIcon("BallWarrior-master/assets/images/background2.gif");
//    }
//
//    @Override
//    protected void update() {
//        int mx = input.getMouseX();
//        int my = input.getMouseY();
//        for (Button button : buttons) {
//            button.setHovered(button.contains(mx, my));
//            if (button.isHovered() && input.consumeClick()) {
//                button.onClick();
//            }
//        }
//    }
//
//    @Override
//    protected void render(Graphics2D g2) {
//        for (Button button : buttons) {
//            button.draw(g2);
//        }
//    }
//
//}
