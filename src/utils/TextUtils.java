package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class TextUtils {

    public static Dimension measureText(String text, Font font) {
        if (text == null || font == null)
            return new Dimension(0, 0);

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();

        g2d.dispose();
        return new Dimension(width, height);
    }
}
