package test.panel;

import core.InputHandler;
import core.ResourceLoader;
import data.SkinData;
import ui.base.Button;
import ui.button.SkinButton;
import utils.ScrollManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GridPanel extends JPanel {
    private final InputHandler input;
    private final SkinGrid skinGrid = new SkinGrid();
    private final ScrollManager scroll = new ScrollManager();
    private final BufferedImage background;
    private final InfoPanel infoPanel;

    public GridPanel(InputHandler input,
                     InfoPanel infoPanel,
                     List<SkinData> skins,
                     AtomicInteger equippedSkinId) {
        this.input = input;
        this.background = ResourceLoader.loadImg("assets/images/shopBg.jpg");
        this.infoPanel = infoPanel;
        setSkins(skins, equippedSkinId);
    }

    public void update() {
        // cập nhật cuộn
        int delta = input.consumeScroll();
        scroll.updateScroll(delta);

        // cập nhật vị trí nút
        skinGrid.updateBounds(scroll.getScrollY(), 80);

        int mx = input.getMouseX() - getX();
        int my = input.getMouseY() - getY();

        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        int clipBottom = clipTop + clipHeight;

        // nếu chuột ngoài vùng
        if (my < clipTop || my > clipBottom) {
            for (Button b : skinGrid.getButtons()) {
                b.setHovered(false);
            }
            return;
        }

        // hover / click
        for (Button b : skinGrid.getButtons()) {
            b.setHovered(b.contains(mx, my));
            if (b.isHovered() && input.consumeClick()) {
                b.onClick();
            }
        }

        // cập nhật max scroll
        int visibleHeight = getHeight() - 95;
        int maxScroll = Math.max(0, skinGrid.calcContentHeight() - visibleHeight);
        scroll.setMaxScroll(maxScroll);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        if (background != null)
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // giới hạn vùng vẽ
        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        Shape oldClip = g2.getClip();
        g2.setClip(new Rectangle(0, clipTop, getWidth(), clipHeight));

        skinGrid.draw(g2);

        g2.setClip(oldClip);
        drawScrollBar(g2);

        g2.dispose();
    }

    private void drawScrollBar(Graphics2D g2) {
        if (!scroll.hasScroll()) return;

        int barX = skinGrid.getGap() + skinGrid.getCols() * (skinGrid.getItemSize() + skinGrid.getGap()) + skinGrid.getGap();
        int barY = 85;
        int barH = getHeight() - 100;
        int contentHeight = skinGrid.calcContentHeight();
        int visibleHeight = getHeight() - 95;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, 10, barH);

        int thumbH = Math.max(30, barH * visibleHeight / contentHeight);
        int thumbY = barY + (int) ((scroll.getScrollY() / (double) scroll.getMaxScroll()) * (barH - thumbH));

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(barX, thumbY, 10, thumbH);
    }

    public void setSkins(List<SkinData> skins, AtomicInteger equippedSkinId) {
        skinGrid.setSkins(skins, equippedSkinId);
        infoPanel.setEquippedSkinId(equippedSkinId);
        for (Button button : skinGrid.getButtons()) {
            button.setActivity(() -> infoPanel.setSkinData(((SkinButton) button).getSkinData()));
            if (((SkinButton) button).getSkinData().getId() == equippedSkinId.get()) {
                infoPanel.setSkinData(((SkinButton) button).getSkinData());
            }
        }
    }
}
