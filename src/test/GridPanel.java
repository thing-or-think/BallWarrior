package test;

import core.InputHandler;
import core.ResourceLoader;
import data.PlayerData;
import data.SkinData;
import entity.Skins;
import ui.base.Button;
import ui.button.SkinButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GridPanel extends JPanel {
    private List<Button> buttons = new ArrayList<>();

    InputHandler input;

    private final int cols = 3;
    private final int itemSize = 100;
    private final int gap = 10;

    private int scrollY = 0;
    private int maxScroll = 0;

    // *
    private BufferedImage background;

    public GridPanel(InputHandler input, List<SkinData> skins) {
        this.input = input;
        this.background = ResourceLoader.loadImg("assets/images/shopBg.jpg");

        setSkins(skins);
        calcMaxScroll();
    }

    private void setSkins(List<SkinData> skinData) {
        if (skinData == null) {
            return;
        }

        for (int i = 0; i < skinData.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = col * (itemSize + gap);
            int y = row * (itemSize + gap);
            buttons.add(new SkinButton(skinData.get(i)));
        }

        updateSkinBounds(0);
    }

    public void update() {
        int scroll = input.consumeScroll();
        if (scroll != 0) {
            scrollY += scroll * 20;
            clampScroll();
            updateSkinBounds(scrollY);
        }

        int mx = input.getMouseX() - getX();
        int my = input.getMouseY() - getY();

        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        int clipBottom = clipTop + clipHeight;

        if (my < clipTop || my > clipBottom) {
            for (Button button : buttons) {
                button.setHovered(false);
            }
            return;
        }

        for (Button button : buttons) {
            button.setHovered(button.contains(mx,my));
            if (button.isHovered() && input.consumeClick()) {
                button.onClick();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        if (background != null) {
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Giới hạn vùng vẽ các ô skin
        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        Shape oldClip = g2.getClip();
        g2.setClip(new Rectangle(0, clipTop, getWidth(), clipHeight));

        // Vẽ danh sách skin

        for (Button button : buttons) {
            button.draw(g2);
        }

        g2.setClip(oldClip);
        drawScrollBar(g2);
        g2.dispose();
    }

    private void clampScroll() {
        scrollY = Math.max(scrollY, 0);
        scrollY = Math.min(scrollY, maxScroll);
    }

    private void updateSkinBounds(int scroll) {
        int x0 = gap;
        int y0 = gap - scroll + 80;

        for (int i = 0; i < buttons.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = x0 + col * (itemSize + gap);
            int y = y0 + row * (itemSize + gap);
            buttons.get(i).setBound(new Rectangle(x, y, itemSize, itemSize));
        }
    }

    private void calcMaxScroll() {
        if (buttons == null) {
            maxScroll = 0;
            return;
        }
        int rows = (int) Math.ceil(buttons.size() / (double) cols);
        int contentHeight = rows * (itemSize + gap);
        int visibleHeight = getHeight() - 95;
        maxScroll = Math.max(0, contentHeight - visibleHeight);
    }

    private void drawScrollBar(Graphics2D g2) {
        if (maxScroll <= 0) return;

        int barX = gap + cols * (itemSize + gap) + gap;
        int barY = 85;
        int barH = getHeight() - 100;
        int contentHeight = rowsHeight();
        int visibleHeight = getHeight() - 95;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, 10, barH);

        int thumbH = Math.max(30, barH * visibleHeight / contentHeight);
        int thumbY = barY + (int) ((scrollY / (double) maxScroll) * (barH - thumbH));

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(barX, thumbY, 10, thumbH);
    }

    private int rowsHeight() {
        if (buttons == null) return 0;
        int rows = (int) Math.ceil(buttons.size() / (double) cols);
        return rows * (itemSize + gap);
    }
}
