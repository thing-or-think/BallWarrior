package ui.panel;

import core.InputHandler;
import core.ResourceLoader;
import data.SkinData;
import ui.base.AnchorType;
import ui.button.BuyButton;
import ui.element.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

public class InfoPanel extends JPanel {
    private final InputHandler input;
    private BuyButton buyButton;
    private SkinData skinData;
    private Label label;
    private AtomicInteger equippedSkinId;
    private AtomicInteger coins;
    private Color color;
    private BufferedImage icon;

    public InfoPanel(InputHandler input,
                     SkinData skinData,
                     AtomicInteger equippedSkinId,
                     AtomicInteger coins) {
        this.input = input;
        this.skinData = skinData;
        this.equippedSkinId = equippedSkinId;
        this.coins = coins;
        setOpaque(false);
    }

    public void init() {
        label = new Label(
                skinData.getName(),
                getWidth() / 2,
                80,
                new Font("Serif", Font.BOLD, 40),
                AnchorType.CENTER_TOP);


        buyButton = new BuyButton(
                "EQUIPPED",
                getWidth() / 2,
                getHeight() - 75,
                new Font("Serif", Font.BOLD, 24),
                AnchorType.CENTER_BASELINE
        );
        buyButton.setActivity(() -> handleClick());
        setSkinData(skinData);
    }

    public void update() {
        int mx = input.getMouseX() - getX();
        int my = input.getMouseY() - getY();

        buyButton.setHovered(buyButton.contains(mx, my));
        if (buyButton.isHovered() && input.consumeClick()) {
            buyButton.onClick();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        buyButton.draw(g2);
        label.draw(g2);
        if ("image".equals(skinData.getDisplay().getType())) {
            if ("ball".equals(skinData.getType())) {
                g2.drawImage(icon, getWidth() / 2 - 100, 200, 200, 200, null);
            } else {
                g2.drawImage(icon, getWidth() / 2 - 150, 300, 300, 70, null);
            }
        } else {
            g2.setColor(color);
            if ("ball".equals(skinData.getType())) {
                g2.fillOval(getWidth() / 2 - 100, 200, 200, 200);
            } else {
                g2.fillRoundRect(getWidth() / 2 - 150, 300, 300, 70,36,36);
            }
        }
    }

    private void loadDisplay() {
        if (skinData == null || skinData.getDisplay() == null) return;

        String type = skinData.getDisplay().getType();
        String value = skinData.getDisplay().getValue();

        if ("color".equalsIgnoreCase(type)) {
            try {
                this.color = Color.decode(value);

                if (color == null) {
                    color = Color.WHITE;
                }

            } catch (Exception e) {
                this.color = Color.WHITE;
            }
        } else if ("image".equalsIgnoreCase(type)) {
            this.icon = ResourceLoader.loadImage(value);
        }
    }


    public void setSkinData(SkinData skinData) {
        this.skinData = skinData;
        if (label != null) {
            switch (skinData.getRarity()) {
                case COMMON:
                    label.setColor(Color.WHITE);
                    break;
                case RARE:
                    label.setColor(Color.CYAN);
                    break;
                case EPIC:
                    label.setColor(Color.MAGENTA);
                    break;
                case LEGENDARY:
                    label.setColor(Color.ORANGE);
                    break;
                default:
                    label.setColor(Color.WHITE);
                    break;
            }
            label.setText(skinData.getName());
        }

        if (buyButton != null) {
            if (this.equippedSkinId.get() == this.skinData.getId()) {
                buyButton.setText("EQUIPPED");
            } else if (this.skinData.isBought()) {
                buyButton.setText("EQUIP");
            } else {
                buyButton.setText(skinData.getPrice() + " 💰");
            }
        }
        loadDisplay();
    }

    public void setEquippedSkinId(AtomicInteger equippedSkinId) {
        this.equippedSkinId = equippedSkinId;
    }

    private void handleClick() {
        if (!skinData.isBought() && coins.get() < skinData.getPrice()) {
            return;
        }
        if (!skinData.isBought()) {
            coins.set(coins.get() - skinData.getPrice());
            skinData.setBought(true);
            equippedSkinId.set(skinData.getId());
        } else if (equippedSkinId.get() != skinData.getId()) {
            equippedSkinId.set(skinData.getId());
        }
        setSkinData(skinData);
    }
}
