package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import data.PlayerData;
import ui.panel.GachaPanel;
import ui.panel.GridPanel;
import ui.panel.InfoPanel;
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
import java.util.concurrent.atomic.AtomicInteger;

public class ShopScene extends Scene {
    private enum Tab { BALLS, PADDLES, GACHA }
    private final List<Button> buttons = new ArrayList<>();
    private final ButtonGroup buttonGroup;
    private final Label moneyLabel;
    private Runnable onBack;
    private GridPanel gridPanel;
    private InfoPanel infoPanel;
    private GachaPanel gachaPanel;

    private Tab currentTab = Tab.BALLS;

    private final static BufferedImage iconBack = ResourceLoader.loadImage("assets/images/Xbutton.png");
    private final static BufferedImage iconBall = ResourceLoader.loadImage("assets/images/iconBall.png");
    private final static BufferedImage iconPaddle = ResourceLoader.loadImage("assets/images/iconPaddle.png");
    private final static BufferedImage iconGacha = ResourceLoader.loadImage("assets/images/iconChest.png");

    private PlayerData playerData;

    public ShopScene(InputHandler input, PlayerData playerData) {
        super("ShopScene", input);
        this.playerData = playerData;
        this.moneyLabel = new Label(null, 690, 10, new Font("Monospaced", Font.BOLD, 22), Color.YELLOW);

        this.buttonGroup = new ButtonGroup();
        AtomicInteger equippedSkinId = playerData.getEquipped().getBallIdRef();

        this.infoPanel = new InfoPanel(
                input,
                playerData.getInventory().getBalls().get(equippedSkinId.get()),
                equippedSkinId,
                playerData.getCoins()
        );
        this.gridPanel = new GridPanel(
                input,
                infoPanel
        );

        this.gachaPanel = new GachaPanel(input, playerData.getCoins(), playerData.getInventory().getItems());

        setLayout(null);

        this.infoPanel.setBounds(Constants.WIDTH / 2, 55, 400, Constants.HEIGHT - 60);
        add(infoPanel);
        this.gridPanel.setBounds(0,55,Constants.WIDTH,Constants.HEIGHT -60);
        add(gridPanel);
        this.gachaPanel.setBounds(0, 55, Constants.WIDTH, Constants.HEIGHT - 60);
        add(gachaPanel);

        initUI();
    }

    @Override
    public void initUI() {
        initButtons();
        handleBalls();
        infoPanel.init();
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

        switch (currentTab) {
            case BALLS:
            case PADDLES:
                gridPanel.update();
                infoPanel.update();
                break;
            case GACHA:
                gachaPanel.update();
                break;
        }
    }
    private void drawTitle(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        g2.drawString(convert(), 20, 110);
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage bar = ResourceLoader.loadImage("assets/images/Bg.png");
        g2.drawImage(bar,0,0,getWidth(),getHeight(),null);
        drawTitle(g2);
        for (Button button : buttons) {
            button.draw(g2);
        }

        moneyLabel.draw(g2);
    }

    private void handleBalls() {
        currentTab = Tab.BALLS;

        gachaPanel.setVisible(false);
        gridPanel.setVisible(true);
        infoPanel.setVisible(true);

        gridPanel.setSkins(
                playerData.getInventory().getBalls(),
                playerData.getEquipped().getBallIdRef()
        );
    }

    private void handlePaddles() {
        currentTab = Tab.PADDLES;

        gachaPanel.setVisible(false);
        gridPanel.setVisible(true);
        infoPanel.setVisible(true);

        gridPanel.setSkins(
                playerData.getInventory().getPaddles(),
                playerData.getEquipped().getPaddleIdRef()
        );
    }

    private void handleGacha() {
        currentTab = Tab.GACHA;

        gachaPanel.setVisible(true);
        gridPanel.setVisible(false);
        infoPanel.setVisible(false);
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    private String convert() {
        if(currentTab == Tab.BALLS) return "BALLS";
        else if (currentTab == Tab.PADDLES) return "PADDLES";
        return "GACHA";
    }
}
