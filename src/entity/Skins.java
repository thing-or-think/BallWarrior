package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import core.ResourceLoader;

enum Rarity {
    COMMON, RARE, EPIC, LEGENDARY;
}
public class Skins {
    private Rarity rarity;
    private String name;
    private Color color;
    private boolean isBought = false;
    private int price;
    private BufferedImage img;

    public Skins(String name,Rarity rarity,int price,boolean isBought,String path) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.img = ResourceLoader.loadImg(path);
    }
    public Skins(String name,Rarity rarity,int price,boolean isBought,Color color) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = color;
    }

}
