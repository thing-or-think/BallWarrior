package ui.scene;

import core.DataChangeListener;
import core.InputHandler;
import core.ResourceLoader;
import core.ResourceSaver;
import data.PlayerData;
import entity.Skins;
import ui.base.ButtonGroup;
import ui.button.IconButton;
import ui.base.Scene;
import ui.base.Button;
import ui.panel.GachaPanel;
import ui.panel.GridPanel;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends Scene implements DataChangeListener {
    private final List<Button> buttons = new ArrayList<>();
    private final ButtonGroup buttonGroup;
    private Runnable onBack;

    private String currentTab = "BALLS";
    private static BufferedImage iconBack = ResourceLoader.loadImage("assets/images/Xbutton.png");
    private static BufferedImage iconBall = ResourceLoader.loadImage("assets/images/iconBall.png");
    private static BufferedImage iconPaddle = ResourceLoader.loadImage("assets/images/iconPaddle.png");
    private static BufferedImage iconGacha = ResourceLoader.loadImage("assets/images/Xbutton.png");

    private final GridPanel gridPanel;
    private final GachaPanel gachaPanel;

    private final PlayerData playerData;

    private List<Skins> ballSkins;
    private List<Skins> paddleSkins;

    private int equippedBallId;
    private int equippedPaddleId;

    public ShopScene(InputHandler input, Runnable onBack) {
        super("ShopScene", input);
        playerData = new PlayerData(ResourceLoader.loadPlayerData());

        this.onBack = onBack;
        this.gridPanel = new GridPanel(input, playerData);
        this.gridPanel.setBounds(0,55,Constants.WIDTH,Constants.HEIGHT -60);
        add(gridPanel);
        this.gridPanel.setShopScene(this);

        this.gachaPanel = new GachaPanel(input, playerData);
        this.gachaPanel.setShopScene(this);
        this.gachaPanel.setDataChangeListener(this);
        this.gachaPanel.setBounds(0, 55, Constants.WIDTH, Constants.HEIGHT - 60);
        this.gachaPanel.setVisible(false);
        this.background = new ImageIcon(ResourceLoader.loadImage("assets/images/topBar.png"));

        buttonGroup = new ButtonGroup();
        add(gachaPanel);
        initUI();
        initButtons();
        initInput();
    }

    @Override
    protected void initUI() {
        setBackground(Color.DARK_GRAY);
        setLayout(null);
        ballSkins = ResourceLoader.loadSkins("docs/balls.txt");
        paddleSkins = ResourceLoader.loadSkins("docs/paddles.txt");
        equippedBallId = ResourceLoader.getEquippedBallId("docs/balls.txt");
        equippedPaddleId = ResourceLoader.getEquippedPaddleId("docs/paddles.txt");
        gridPanel.setSkins(ballSkins);
    }

    /** Tạo các button */
    private void initButtons() {
        buttons.add(new IconButton("BACK",iconBack,20,0,50,50,() -> onBack.run()));
        buttons.add(new IconButton("BALLS",iconBall,250,0,50,50,() -> handleBalls()));
        buttons.add(new IconButton("PADDLES",iconPaddle,350,0,50,50,() -> handlePaddles()));
        buttons.add(new IconButton("GACHA",iconGacha,450,0,50,50,() -> handleGacha()));
        Color color = new Color(255, 255, 0, 100);
        for (Button button : buttons) {
            if (button.getText().equals("BACK")) {
                button.setColor(new Color(0,0,0,0));
                continue;
            }
            button.setColor(color);
            buttonGroup.add((IconButton) button);
            ((IconButton) button).setButtonGroup(buttonGroup);
        }
        buttons.get(1).setClicked(true);
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.setHovered(button.contains(mx,my));
            if (button.isHovered() && input.consumeClick()) {
                handleButtonClick(button.getText());
            }
        }
    }

    /** Xử lý sự kiện click button */
    private void handleButtonClick(String text) {
        System.out.println("Clicked " + text);
        gridPanel.setVisible(true);
        gachaPanel.setVisible(false);
        switch (text) {
            case "BACK":
                onBack.run();
                break;
            case "BALLS":
                currentTab = "BALLS";
                gridPanel.setSkins(ballSkins);
                gridPanel.setIsBall(true);
                gridPanel.setCurrentTab(currentTab);
                break;
            case "PADDLES":
                currentTab = "PADDLES";
                gridPanel.setSkins(paddleSkins);
                gridPanel.setIsBall(false);
                gridPanel.setCurrentTab(currentTab);
                break;
            case "GACHA":
                currentTab = "GACHA";
                gridPanel.setVisible(false);
                gachaPanel.setVisible(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDataChanged() {
        System.out.println("Dữ liệu Skins đã thay đổi. Đang tải lại danh sách Skins...");

        this.ballSkins = ResourceLoader.loadSkins("docs/balls.txt");
        this.paddleSkins = ResourceLoader.loadSkins("docs/paddles.txt");

        // CẬP NHẬT ID TRANG BỊ MỚI NHẤT
        this.equippedBallId = ResourceLoader.getEquippedBallId("docs/balls.txt");
        this.equippedPaddleId = ResourceLoader.getEquippedPaddleId("docs/paddles.txt");

        ResourceSaver.savePlayerData(playerData);

        // CẬP NHẬT GRIDPANEL ĐỂ HIỂN THỊ DỮ LIỆU MỚI
        if (currentTab.equals("BALLS")) {
            gridPanel.setSkins(this.ballSkins);
        } else if (currentTab.equals("PADDLES")) {
            gridPanel.setSkins(this.paddleSkins);
        }
        repaint();
    }

    /** Vẽ background */
    @Override
    protected void drawBackground(Graphics2D g2) {
        BufferedImage bar = ResourceLoader.loadImage("assets/images/topBar.png");
        g2.drawImage(bar,0,0,800,50,null);
    }

    /** Vẽ button */
    private void drawButtons(Graphics2D g2) {
        for (Button button : buttons) {
            if (button.getText() == currentTab) {
                g2.setColor(new Color(255, 255, 0, 100));
                g2.fillRect(button.getBounds().x,button.getBounds().y,50,50);
            }
            button.draw(g2);
        }
    }

    /** 💰 Vẽ tiền người chơi có */
    private void drawMoney(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        int money = playerData.getCoins().get();
        g2.drawString(money + "\uD83D\uDCB0",690,30);
    }

    @Override
    protected void render(Graphics2D g2) {
        update();
        drawBackground(g2);
        drawMoney(g2);
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    private void handleBalls() {
        currentTab = "BALLS";
        gridPanel.setVisible(true);
        gachaPanel.setVisible(false);
        gridPanel.setSkins(ballSkins);
        gridPanel.setIsBall(true);
        gridPanel.setCurrentTab(currentTab);
    }

    private void handlePaddles() {
        currentTab = "PADDLES";
        gridPanel.setVisible(true);
        gachaPanel.setVisible(false);
        gridPanel.setSkins(paddleSkins);
        gridPanel.setIsBall(false);
        gridPanel.setCurrentTab(currentTab);
    }

    private void handleGacha() {
        currentTab = "GACHA";
        gridPanel.setVisible(false);
        gachaPanel.setVisible(true);
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
    // <<< GETTER MỚI DÙNG CHO GRIDPANEL >>>
    public String getCurrentTab() {
        return currentTab;
    }
    public int getEquippedBallId() {
        return equippedBallId;
    }
    public int getEquippedPaddleId() {
        return equippedPaddleId;
    }
    public List<Skins> getBallSkins() {
        return ballSkins;
    }
    public List<Skins> getPaddleSkins() {
        return paddleSkins;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }
}