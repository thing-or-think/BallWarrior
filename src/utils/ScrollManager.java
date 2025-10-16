package utils;

public class ScrollManager {
    private int scrollY = 0;
    private int maxScroll = 0;

    public void updateScroll(int delta) {
        if (delta != 0) {
            scrollY += delta * 20;
            clampScroll();
        }
    }

    public void setMaxScroll(int value) {
        maxScroll = Math.max(0, value);
        clampScroll();
    }

    private void clampScroll() {
        scrollY = Math.max(0, Math.min(scrollY, maxScroll));
    }

    public int getScrollY() {
        return scrollY;
    }

    public int getMaxScroll() {
        return maxScroll;
    }

    public boolean hasScroll() {
        return maxScroll > 0;
    }
}