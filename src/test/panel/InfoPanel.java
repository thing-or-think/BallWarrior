package test.panel;

import core.InputHandler;
import data.SkinData;
import ui.base.AnchorType;
import ui.button.BuyButton;
import ui.element.Label;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InfoPanel extends JPanel {
    private final InputHandler input;
    private BuyButton buyButton;
    private SkinData skinData;
    private Label label;
    private AtomicInteger equippedSkinId;

    public InfoPanel(InputHandler input, SkinData skinData, AtomicInteger equippedSkinId) {
        this.input = input;
        this.skinData = skinData;
        this.equippedSkinId = equippedSkinId;
        setOpaque(false);
    }

    public void init() {
        label = new Label(
                skinData.getName(),
                getWidth() / 2,
                80,
                new Font("Serif", Font.BOLD, 32),
                AnchorType.CENTER_TOP);


        buyButton = new BuyButton(
                "EQUIPPED",
                getWidth() / 2,
                getHeight() - 75,
                new Font("Serif", Font.BOLD, 24),
                AnchorType.CENTER_BASELINE
        );
        setSkinData(skinData);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        buyButton.draw(g2);
        label.draw(g2);
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
                buyButton.setText("BUY");
            }
        }
    }

    public void setEquippedSkinId(AtomicInteger equippedSkinId) {
        this.equippedSkinId = equippedSkinId;
    }
}
