package ui.scene;

import core.InputHandler;
import ui.base.Button;
import ui.base.Scene;
import ui.button.TextButton;
import utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends Scene {
    private final List<Button> buttons = new ArrayList<>();
    private Runnable onBack;
    private boolean mouseHandled = false;

    public ShopScene(InputHandler input, Runnable onBack) {
        super("ShopScene", input);
        this.onBack = onBack;

        initUI();

    }

    @Override
    protected void initUI() {
        setBackground(Color.DARK_GRAY);

        Font font = new Font("Serif", Font.PLAIN, 28);
        FontMetrics fm = getFontMetrics(font);

        String[] items = {"Speed +5", "Bigger Paddle", "Extra Life"};
        int startY = 200;
        int spacing = 50;

        for (int i = 0; i < items.length; i++) {
            buttons.add(new TextButton(items[i], Constants.WIDTH / 2, startY + i * spacing, fm));
        }

        buttons.add(new TextButton("BACK", Constants.WIDTH / 2, startY + items.length * spacing + 50, fm));
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));

            if (button.isHovered() && input.isMousePressed() && !mouseHandled) {
                mouseHandled = true;
                System.out.println("Clicked: " + button.getText());

                if (button.getText().equals("BACK")) {
                    input.resetMouse();
                    onBack.run();
                } else {
                    System.out.println("Mua: " + button.getText());
                }
            }
        }

        if (!input.isMousePressed()) {
            mouseHandled = false;
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        g2.setFont(new Font("Serif", Font.PLAIN, 28));
        g2.setColor(Color.WHITE);
        g2.drawString("SHOP", Constants.WIDTH / 2 - 40, 150);

        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
