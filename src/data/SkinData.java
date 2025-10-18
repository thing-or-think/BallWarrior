package data;

import entity.Rarity;

public class SkinData {
    private int id;
    private String type;
    private String name;
    private Rarity rarity;
    private int price;
    private boolean isBought;
    public Display display;

    public static class Display {
        public String type; // "color" hoặc "image"
        public String value; // màu hoặc path ảnh
    }

    public String getType() { return type; }
    public int getId() { return id; }
    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public int getPrice() { return price; }
    public boolean isBought() { return isBought; }
    public Display getDisplay() { return display; }
}
