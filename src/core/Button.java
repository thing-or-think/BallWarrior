package core;

import java.awt.*;

public class Button {
    public String text;
    public Rectangle bound;
    public boolean hovered;
    public Font baseFont;


    public Button(String text,int x,int y,FontMetrics fm) {
        this.text = text;
        int width = fm.stringWidth(text);
        int height = fm.getAscent();
        int Rx = x-width/2; // x la giua man hinh
        int Ry = y;
        this.bound = new Rectangle(Rx,Ry-height,width,height);
        this.baseFont = new Font("Serif", Font.PLAIN, 32);
    }
    public void draw(Graphics2D g) {
        g.setFont(new Font("Serif",Font.PLAIN, 32));
        //khi chuot tro toi button
        if (hovered) {

            Font hoverFont = baseFont.deriveFont(Font.PLAIN, 40f);
            g.setFont(hoverFont);
            g.setColor(Color.YELLOW);
            String decoratedText = "> " + text + " <";

            int textWidth = g.getFontMetrics().stringWidth(decoratedText);
            int drawX = (Constants.WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(decoratedText, drawX, drawY);
        } else {
            g.setFont(baseFont);
            g.setColor(Color.WHITE);

            int textWidth = g.getFontMetrics().stringWidth(text);
            int drawX = (Constants.WIDTH - textWidth) / 2;
            int drawY = bound.y + bound.height;

            g.drawString(text, drawX, drawY);
        }
    }
    public boolean contains(int mx,int my) {
        return bound.contains(mx,my);
    }
    public int getY() {
        return this.bound.y; // nếu bạn có biến y lưu vị trí nút
    }

}
