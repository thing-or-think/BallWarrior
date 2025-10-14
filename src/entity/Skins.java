package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import core.ResourceLoader;

public class Skins {
    private final int id;
    private final String name;
    private final Rarity rarity;
    private final int price;
    private boolean isBought = false;
    private String colorOrPath;
    private final Color color;
    private Rectangle bounds;
    //Img
    private BufferedImage img;
    private static final BufferedImage common = ResourceLoader.loadImg("assets/images/CommonBg.jpg");
    private static final BufferedImage rare = ResourceLoader.loadImg("assets/images/RareBg.jpg");
    private static final BufferedImage epic = ResourceLoader.loadImg("assets/images/EpicBg.jpg");
    private static final BufferedImage legendary = ResourceLoader.loadImg("assets/images/LegendaryBg.jpg");

    /** skin phai them anh */
    public Skins(int id, String name,Rarity rarity,int price,boolean isBought,String path) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = Color.WHITE;
        this.img = ResourceLoader.loadImg(path);
        this.bounds = null;
    }
    /** skin binh thuong */
    public Skins(int id, String name,Rarity rarity,int price,boolean isBought,Color color) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = color;
        this.img = null;
        this.bounds = null;
    }
    /** ve ô skin */
    public void draw (Graphics2D g2,int x,int y,int w,int h, boolean isBall,int id) {
        // ve nen theo rarity
        switch (rarity) {
            case COMMON -> g2.drawImage(common,x,y,w,h,null);
            case RARE -> g2.drawImage(rare,x,y,w,h,null);
            case EPIC -> g2.drawImage(epic,x,y,w,h,null);
            case LEGENDARY -> g2.drawImage(legendary,x,y,w,h,null);
        }
        //ve skin
        if (img == null) {
            g2.setColor(color != null ? color : Color.WHITE);
            if (isBall) {
                g2.fillOval(x + w/4,y + h/4 - 10, w/2, h/2);
            } else {
                g2.fillRect(x+w/2-30, y+h/2-10, 60, 20);
            }
        } else {
            if (isBall) {
                g2.drawImage(img, x + w/4,y + h/4 - 10, w/2, h/2, null);
            } else {
                g2.drawImage(img, x+ 10, y + h/2 - 10, w - 20, 20, null);
            }
        }

        if (!isBought()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, w, h);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Serif", Font.BOLD, 20));
            g2.drawString(price + "\uD83D\uDCB0", x + w/3, y + h - 10);
        } else {
            // Logic khi đã mua (isBought = true)
        }

        if (this.id == id) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(5f));
            g2.drawRect(x, y, w, h);

            g2.setFont(new Font("Monospaced",Font.BOLD,18));
            g2.drawString("EQUIPPED",x + 6, y + h - 10);
        }
    }

    public boolean contains(int mx, int my) {
        return bounds != null && bounds.contains(mx, my);
    }

    // setter, getter
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isBought() { return isBought; }
    public BufferedImage getImg() { return img; }
    public Color getColor() { return color; }
    public int getPrice() { return price; }
    public Rarity getRarity() { return rarity; }
    public Rectangle getBounds() { return bounds;}

    public void setBounds(Rectangle bounds) { this.bounds = bounds;}
    public void setBought(boolean bought) {
        this.isBought = bought;
    }
}
