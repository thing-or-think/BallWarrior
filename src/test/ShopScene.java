package test;

import core.InputHandler;
import core.ResourceLoader;
import data.PlayerData;
import ui.base.Button;
import ui.base.ButtonGroup;
import ui.base.Scene;
import ui.button.IconButton;
import ui.element.Label;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends Scene {
    private final List<Button> buttons = new ArrayList<>();
    private final ButtonGroup buttonGroup;
    private final Label moneyLabel;
    private Runnable onBack;
    private GridPanel gridPanel;

    private String currentTab = "BALLS";

    private final static BufferedImage iconBack = ResourceLoader.loadImg("assets/images/Xbutton.png");
    private final static BufferedImage iconBall = ResourceLoader.loadImg("assets/images/iconBall.png");
    private final static BufferedImage iconPaddle = ResourceLoader.loadImg("assets/images/iconPaddle.png");
    private final static BufferedImage iconGacha = ResourceLoader.loadImg("assets/images/Xbutton.png");

    private PlayerData playerData;

    public ShopScene(InputHandler input, PlayerData playerData) {
        super("ShopScene", input);
        this.playerData = playerData;
        this.moneyLabel = new Label(null, 690, 10, new Font("Monospaced", Font.BOLD, 22), Color.YELLOW);

        this.buttonGroup = new ButtonGroup();
        this.gridPanel = new GridPanel(input, playerData.getInventory().getBalls());
        setLayout(null);
        this.gridPanel.setBounds(0,55,Constants.WIDTH,Constants.HEIGHT -60);
        add(gridPanel);
        initUI();
    }

    @Override
    public void initUI() {
        initButtons();
    }

    private void initButtons() {
        buttons.add(new IconButton("BACK",iconBack,20,0,50,50,() -> onBack.run()));
        buttons.add(new IconButton("BALLS",iconBall,250,0,50,50,() -> handleBalls()));
        buttons.add(new IconButton("PADDLES",iconPaddle,350,0,50,50,() -> handlePaddles()));
        buttons.add(new IconButton("GACHA",iconGacha,450,0,50,50,() -> handleGacha()));
        for (Button button : buttons) {
            if (button.getText().equals("BACK")) {
                button.setColor(new Color(0,0,0,0));
                continue;
            }
            button.setColor(new Color(255, 255, 0, 100));
            buttonGroup.add(button);
            ((IconButton) button).setButtonGroup(buttonGroup);
        }
        buttons.get(1).setClicked(true);
    }

    @Override
    public void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.setHovered(button.contains(mx,my));
            if (button.isHovered() && input.consumeClick()) {
                button.onClick();
            }
        }

        String moneyText = playerData.getCoins() + "\uD83D\uDCB0";
        if (!moneyLabel.getText().equals(moneyText)) {
            moneyLabel.setText(moneyText);
        }

        gridPanel.update();
    }

    @Override
    public void render(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }

        moneyLabel.draw(g2);
    }

    private void handleBalls() {
    }

    private void handlePaddles() {
    }

    private void handleGacha() {
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
