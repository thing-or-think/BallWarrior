package test.panel;

import core.InputHandler;
import data.SkinData;
import ui.button.BuyButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InfoPanel extends JPanel {
    private final InputHandler input;
    private final BuyButton buyButton;
    private SkinData skinData;

    public InfoPanel(InputHandler input) {
        this.input = input;
        buyButton = new BuyButton("BUY",120,420,160,50,new Font("Serif", Font.BOLD, 24));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    }

    public void setSkinData(SkinData skinData) {
        this.skinData = skinData;
    }
}
