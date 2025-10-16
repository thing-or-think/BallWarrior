package test.panel;

import ui.base.Button;
import ui.button.SkinButton;
import data.SkinData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkinGrid {
    private final int cols = 3;
    private final int itemSize = 100;
    private final int gap = 10;

    private final List<Button> buttons = new ArrayList<>();

    public void setSkins(java.util.List<SkinData> skinData) {
        buttons.clear();
        if (skinData == null) return;

        for (SkinData data : skinData) {
            buttons.add(new SkinButton(data));
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

    public java.util.List<Button> getButtons() {
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
