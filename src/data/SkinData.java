package data;

import entity.Rarity;

public class SkinData {
    private int id;
    private String type;
    private String name;
    private Rarity rarity;
    private int price;
    private boolean isBought;
    private Display display;

    public static class Display {
        protected String type; // "color" hoặc "image"
        protected String value; // màu hoặc path ảnh
        public String getType() {
            return type;
        }
        public String getValue() {
            return value;
        }
    }

    public String getType() { return type; }
    public int getId() { return id; }
    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public int getPrice() { return price; }
    public boolean isBought() { return isBought; }
    public Display getDisplay() { return display; }
    public void setBought(boolean isBought) {
        this.isBought = isBought;
    }
}
