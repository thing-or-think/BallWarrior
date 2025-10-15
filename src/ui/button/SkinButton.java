package ui.button;

import core.ResourceLoader;
import data.SkinData;
import entity.Rarity;
import ui.base.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SkinButton extends Button {
    private SkinData skinData;

    private static final BufferedImage common = ResourceLoader.loadImg("assets/images/CommonBg.jpg");
    private static final BufferedImage rare = ResourceLoader.loadImg("assets/images/RareBg.jpg");
    private static final BufferedImage epic = ResourceLoader.loadImg("assets/images/EpicBg.jpg");
    private static final BufferedImage legendary = ResourceLoader.loadImg("assets/images/LegendaryBg.jpg");

    public SkinButton(int x, int y, int width, int height, SkinData skinData) {
        super(null, null, x, y, width, height);
        this.skinData = skinData;
    }

    public SkinButton(int x, int y, int width, int height, SkinData skinData, Runnable activity) {
        super(null, null, x, y, width, height);
        this.activity = activity;
        this.skinData = skinData;
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (skinData.getRarity()) {
            case COMMON -> g2.drawImage(common, x, y, width, height, null);
            case RARE -> g2.drawImage(rare, x, y, width, height, null);
            case EPIC -> g2.drawImage(epic, x, y, width, height, null);
            case LEGENDARY -> g2.drawImage(legendary, x, y, width, height, null);
        }

        if (skinData.display.type == "color") {
            g2.setColor(skinData.display.value != null ? color : Color.WHITE);
            g2.fillOval(x + width / 4, y + height / 4 - 10, width / 2, height / 2);
        } else {
            g2.drawImage(ResourceLoader.loadImg(skinData.display.value), x + width / 4, y + height / 4 - 10, width / 2, height / 2, null);
        }

        if (!skinData.isBought()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, width, height);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Serif", Font.BOLD, 18));
            g2.drawString(skinData.getPrice() + " 💰", x + width / 3, y + height - 10);
        }

        if (hovered) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(x, y, width, height);
        }

        if (clicked) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(4f));
            g2.drawRect(x, y, width, height);

            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString("EQUIPPED", x + 6, y + height - 10);
        }
    }

    @Override
    public void onClick() {
        activity.run();
        clicked = true;
    }
}
