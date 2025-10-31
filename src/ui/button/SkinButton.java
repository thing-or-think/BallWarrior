package ui.button;

import core.ResourceLoader;
import data.SkinData;
import ui.base.Button;
import entity.Rarity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

public class SkinButton extends Button {
    private boolean isEquipped;
    private final SkinData skinData;
    private static AtomicInteger equippedSkinId;

    private static final BufferedImage COMMON_BG = ResourceLoader.loadImage("assets/images/Bg/CommonBg.jpg");
    private static final BufferedImage RARE_BG = ResourceLoader.loadImage("assets/images/Bg/RareBg.jpg");
    private static final BufferedImage EPIC_BG = ResourceLoader.loadImage("assets/images/Bg/EpicBg.jpg");
    private static final BufferedImage LEGENDARY_BG = ResourceLoader.loadImage("assets/images/Bg/LegendaryBg.jpg");

    // Constructors
    public SkinButton(int x, int y, int width, int height, SkinData skinData) {
        super("SkinButton", null, x, y, width, height);
        this.skinData = skinData;
        loadDisplay();
    }

    public SkinButton(int x, int y, int width, int height, SkinData skinData, Runnable activity) {
        super("SkinButton", null, x, y, width, height);
        this.activity = activity;
        this.skinData = skinData;
        loadDisplay();
    }

    public SkinButton(SkinData skinData) {
        super("SkinButton", null, 0, 0, 0, 0);
        this.skinData = skinData;
        loadDisplay();
    }

    private void loadDisplay() {
        if (skinData == null || skinData.getDisplay() == null) return;
        String type = skinData.getDisplay().getType();
        String value = skinData.getDisplay().getValue();
        if ("color".equalsIgnoreCase(type)) {
            try {
                this.color = Color.decode(value);
            } catch (Exception e) { this.color = Color.WHITE; }
        } else if ("image".equalsIgnoreCase(type)) {
            this.icon = ResourceLoader.loadImage(value);
        }
    }

    private static BufferedImage getBackgroundByRarity(SkinData skinData) {
        if (skinData == null) return null;
        return switch (skinData.getRarity()) {
            case COMMON -> COMMON_BG;
            case RARE -> RARE_BG;
            case EPIC -> EPIC_BG;
            case LEGENDARY -> LEGENDARY_BG;
        };
    }

    private static Color parseColorValue(String value) {
        if (value == null) return Color.WHITE;
        try {
            return Color.decode(value);
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

    public static void simpleDraw(Graphics2D g2, SkinData skinData, int x, int y, int width, int height) {
        if (skinData == null) return;
        // rarity Bg
        g2.drawImage(getBackgroundByRarity(skinData), x, y, width, height, null);

        String displayType = skinData.getDisplay().getType();
        String displayValue = skinData.getDisplay().getValue();

        if ("color".equalsIgnoreCase(displayType)) {    //color
            Color color = parseColorValue(displayValue);
            g2.setColor(color);

            if ("ball".equalsIgnoreCase(skinData.getType())) {
                g2.fillOval(x + width / 4, y + height / 4 - height/10, width / 2, height / 2);
            } else {
                g2.fillRect(x + width/2 - 3*width/8, y + height/2 - height/7, 3*width/4, height/5);
            }
        }
        else if ("image".equalsIgnoreCase(displayType)) {   //img
            BufferedImage icon = ResourceLoader.loadImage(displayValue);
            if (icon != null) {
                if ("ball".equalsIgnoreCase(skinData.getType())) {
                    g2.drawImage(icon, x + width / 4, y + height / 4 - height/10, width / 2, height / 2, null);
                } else {
                    g2.drawImage(icon, x+width/2-3*width/8, y+height/2-height/10, 3*width/4, height/5, null);
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (skinData == null) return;
        isEquipped = (equippedSkinId != null && skinData.getId() == equippedSkinId.get());

        simpleDraw(g2, skinData, x, y, width, height);

        // Nếu chưa mua
        if (!skinData.isBought()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, width, height);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Serif", Font.BOLD, 18));
            g2.drawString(skinData.getPrice() + " 💰", x + width / 3, y + height - 10);
        }

        // Hover hiệu ứng
        if (hovered) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(x, y, width, height);
        }

        // Click (đang trang bị)
        if (isEquipped) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(4f));
            g2.drawRect(x, y, width, height);

            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString("EQUIPPED", x + 6, y + height - 10);
        }

        if (clicked && !isEquipped) {
            g2.setColor(new Color(0, 255, 0, 120));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(x, y, width, height);
        }
    }

    @Override
    public void onClick() {
        if (buttonGroup != null) {
            buttonGroup.select(this);
        } else {
            clicked = true;
        }
        if (activity != null) {
            activity.run();
        }
        clicked = true;
    }

    public SkinData getSkinData() {
        return skinData;
    }

    public static void setEquippedSkinId(AtomicInteger equippedSkinId) {
        SkinButton.equippedSkinId = equippedSkinId;
    }
}
