package ui.button;

import core.ResourceLoader;
import data.SkinData;
import entity.Rarity;
import ui.base.Button;
import ui.base.ButtonGroup;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SkinButton extends Button {
    private final SkinData skinData;

    private static final BufferedImage COMMON_BG = ResourceLoader.loadImg("assets/images/CommonBg.jpg");
    private static final BufferedImage RARE_BG = ResourceLoader.loadImg("assets/images/RareBg.jpg");
    private static final BufferedImage EPIC_BG = ResourceLoader.loadImg("assets/images/EpicBg.jpg");
    private static final BufferedImage LEGENDARY_BG = ResourceLoader.loadImg("assets/images/LegendaryBg.jpg");

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
        if (skinData == null || skinData.display == null) return;

        String type = skinData.display.type;
        String value = skinData.display.value;

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
            this.icon = ResourceLoader.loadImg(value);
        }
    }

    private BufferedImage getBackgroundByRarity() {
        return switch (skinData.getRarity()) {
            case COMMON -> COMMON_BG;
            case RARE -> RARE_BG;
            case EPIC -> EPIC_BG;
            case LEGENDARY -> LEGENDARY_BG;
        };
    }

    @Override
    public void draw(Graphics2D g2) {
        if (skinData == null) return;

        // Nền theo độ hiếm
        g2.drawImage(getBackgroundByRarity(), x, y, width, height, null);

        // Vẽ phần hiển thị skin (đã được loadDisplay() chuẩn bị sẵn)
        if (!Color.WHITE.equals(color)) {
            g2.setColor(color);
            if (skinData.getType().equals("ball")) {
                g2.fillOval(x + width / 4, y + height / 4 - 10, width / 2, height / 2);
            } else {
                g2.fillRect(x + width/2 - 30, y + height/2 - 10, 60, 20);  // <-- Vẽ paddle (hình chữ nhật)
            }
        } else if (icon != null) {
            if (skinData.getType().equals("ball")) {
                g2.drawImage(icon, x + width / 4, y + height / 4 - 10, width / 2, height / 2, null);
            } else {
                g2.drawImage(icon, x + 10, y + height/2 - 10, width - 20, 20, null);  // <-- Vẽ paddle từ ảnh

            }
        }

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
        if (buttonGroup != null) {
            buttonGroup.select(this);
        } else {
            clicked = true;
        }
        if (activity != null) activity.run();
        clicked = true;
    }
}
