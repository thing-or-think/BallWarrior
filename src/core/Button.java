package core;

import utils.Constants;
import core.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {
    public String text;
    public Rectangle bound;
    public boolean hovered;
    private final Font baseFont;
    private final boolean centered; // true = menu style, false = rectangular style
    private final BufferedImage icon;

    /** Dùng cho menu chữ (căn giữa theo WIDTH) */
    public Button(String text, int x, int y, FontMetrics fm, boolean middle) {
        this.text = text;
        int width = fm.stringWidth(text);
        int height = fm.getAscent();
        int Rx = middle ? x - width / 2 : x;
        int Ry = y;
        this.bound = new Rectangle(Rx, Ry - height, width, height);
        this.baseFont = new Font("Serif", Font.PLAIN, 32);
        this.centered = true;
        this.icon = null;
    }

    /** Dùng cho shop chữ (nút hình chữ nhật cố định) */
    public Button(String text, int x, int y, int w, int h) {
        this.text = text;
        this.bound = new Rectangle(x, y, w, h);
        this.baseFont = new Font("Serif", Font.PLAIN, 24);
        this.centered = false;
        this.icon = null;
    }

    /** Dùng cho shop icon */
    public Button(String text, BufferedImage icon, int x, int y, int w, int h) {
        this.text = text;
        this.icon = icon;
        this.bound = new Rectangle(x,y,w,h);
        this.baseFont = new Font("Serif", Font.PLAIN, 24);
        this.centered = false;
    }

    public void draw(Graphics2D g) {
        if (icon != null) {
            // --- Icon button ---
            g.drawImage(icon, bound.x, bound.y, bound.width, bound.height, null);
//            if (hovered) {
//                g.setColor(new Color(255, 255, 0, 100));
//                g.fillRect(bound.x, bound.y, bound.width, bound.height);
//            }
        } else if (centered) {
            // --- Menu style ---
            g.setFont(baseFont);
            if (hovered) {
                Font hoverFont = baseFont.deriveFont(Font.PLAIN, 40f);
                g.setFont(hoverFont);
                g.setColor(Color.YELLOW);
                String decorated = "> " + text + " <";
                int textWidth = g.getFontMetrics().stringWidth(decorated);
                int drawX = (Constants.WIDTH - textWidth) / 2;
                int drawY = bound.y + bound.height;
                g.drawString(decorated, drawX, drawY);
            } else {
                g.setColor(Color.WHITE);
                int textWidth = g.getFontMetrics().stringWidth(text);
                int drawX = (Constants.WIDTH - textWidth) / 2;
                int drawY = bound.y + bound.height;
                g.drawString(text, drawX, drawY);
            }
        } else {
            // --- Shop style (rectangular tab) ---
            g.setColor(Color.WHITE);
            g.drawRect(bound.x, bound.y, bound.width, bound.height);
            if (hovered) {
                g.setColor(new Color(200,200,0,80));
                g.fillRect(bound.x, bound.y, bound.width, bound.height);
                g.setColor(Color.BLACK);
            }

            // căn giữa text trong ô
            if (text != null) {
                g.setFont(baseFont);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int tx = bound.x + (bound.width - textWidth) / 2;
                int ty = bound.y + (bound.height + textHeight) / 2 - 4;
                g.drawString(text, tx, ty);
            }
        }
    }

    public boolean contains(int mx, int my) {
        return bound.contains(mx, my);
    }
}

