package ui.panel.panel;

import core.InputHandler;
import data.SkinData;
import ui.base.Button;
import ui.button.BuyButton;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GachaPanel extends JPanel {
    private List<SkinData> items;
    private final InputHandler input;
    private Button spinButton;
    private AtomicInteger coins;
    private final int COST = 1000;
    private Random rand = new Random();

    public GachaPanel(InputHandler input,
                      AtomicInteger coins,
                      List<SkinData> items) {
        this.input = input;
        this.items = items;
        this.coins = coins;
        setOpaque(false);

        initUI();
    }

    private void initUI() {
        Font font = new Font("Serif", Font.PLAIN, 32);
        spinButton = new BuyButton("SPIN", Constants.WIDTH/2-50,400,100,50,font,() -> handleGachaSpin());
    }

    private SkinData openGacha() {
        int randomIndex = rand.nextInt(items.size());
        return items.get(randomIndex);
    }

    private void handleGachaSpin() {
        if (coins.get() < COST) {
            System.out.println("KHÔNG ĐỦ TIỀN!");
            return;
        }

        coins.set(coins.get() - COST);
        SkinData awardedSkin = openGacha();

        if (awardedSkin.isBought()) {
            System.out.println("✨ TRÙNG SKIN: " + awardedSkin.getName() + " phải CHỊU");
        } else {
            awardedSkin.setBought(true);
        }
    }

    public void update() {
        int mx = input.getMouseX() - getX();
        int my = input.getMouseY() - getY();

        spinButton.setHovered(spinButton.contains(mx, my));
        if (spinButton.isHovered() && input.consumeClick()) {
            spinButton.onClick();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        spinButton.draw(g2);
    }

}
