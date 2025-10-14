package ui.button;

import core.ResourceLoader;
import entity.Rarity;
import ui.base.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SkinButton extends Button {
    private final int id;
    private final String name;
    private final Rarity rarity;
    private final int price;
    private boolean isBought;
    private final Color color;
    private final BufferedImage img;

    private static final BufferedImage common = ResourceLoader.loadImg("assets/images/CommonBg.jpg");
    private static final BufferedImage rare = ResourceLoader.loadImg("assets/images/RareBg.jpg");
    private static final BufferedImage epic = ResourceLoader.loadImg("assets/images/EpicBg.jpg");
    private static final BufferedImage legendary = ResourceLoader.loadImg("assets/images/LegendaryBg.jpg");

    public SkinButton(int id, String name, Rarity rarity, int price, boolean isBought, String imgPath,
                      int x, int y, int width, int height, Runnable activity) {
        super(name, null, x, y, width, height);
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = Color.WHITE;
        this.img = ResourceLoader.loadImg(imgPath);
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (rarity) {
            case COMMON -> g2.drawImage(common, x, y, width, height, null);
            case RARE -> g2.drawImage(rare, x, y, width, height, null);
            case EPIC -> g2.drawImage(epic, x, y, width, height, null);
            case LEGENDARY -> g2.drawImage(legendary, x, y, width, height, null);
        }

        if (img == null) {
            g2.setColor(color != null ? color : Color.WHITE);
            g2.fillOval(x + width / 4, y + height / 4 - 10, width / 2, height / 2);
        } else {
            g2.drawImage(img, x + width / 4, y + height / 4 - 10, width / 2, height / 2, null);
        }

        if (!isBought) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, width, height);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Serif", Font.BOLD, 18));
            g2.drawString(price + " 💰", x + width / 3, y + height - 10);
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
