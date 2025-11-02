package ui.panel;

import ui.base.Button;
import ui.base.ButtonGroup;
import ui.button.SkinButton;
import data.SkinData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SkinGrid {
    private final int cols = 3;
    private final int itemSize = 130;
    private final int gap = 20;

    private final List<Button> buttons = new ArrayList<>();
    private final ButtonGroup buttonGroup = new ButtonGroup();

    private AtomicInteger equippedSkinId;

    public void setSkins(List<SkinData> skinData, AtomicInteger equippedSkinId) {
        buttons.clear();
        buttonGroup.removeAll();
        this.equippedSkinId = equippedSkinId;
        SkinButton.setEquippedSkinId(equippedSkinId);
        if (skinData == null) return;

        for (SkinData data : skinData) {
            SkinButton skinButton = new SkinButton(data);

            buttons.add(skinButton);
            buttonGroup.add(skinButton);
            skinButton.setButtonGroup(buttonGroup);
        }
    }

    public void updateBounds(int scrollY, int offsetY) {
        int x0 = gap;
        int y0 = gap - scrollY + offsetY;

        for (int i = 0; i < buttons.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = x0 + col * (itemSize + gap);
            int y = y0 + row * (itemSize + gap);
            buttons.get(i).setBound(new Rectangle(x, y, itemSize, itemSize));
        }
    }

    public void draw(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public int calcContentHeight() {
        int rows = (int) Math.ceil(buttons.size() / (double) cols);
        return rows * (itemSize + gap);
    }

    public int getCols() {
        return cols;
    }

    public int getItemSize() {
        return itemSize;
    }

    public int getGap() {
        return gap;
    }
}
