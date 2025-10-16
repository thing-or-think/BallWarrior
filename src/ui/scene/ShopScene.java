package ui.scene;

import core.DataChangeListener;
import core.InputHandler;
import core.ResourceLoader;
import entity.Skins;
import ui.button.IconButton;
import ui.base.Scene;
import ui.base.Button;
import ui.button.IconButton;
import ui.button.MenuButton;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends Scene implements DataChangeListener {
    private final List<Button> buttons = new ArrayList<>();
    private Runnable onBack;

    private String currentTab = "BALLS";
    private static BufferedImage iconBack = ResourceLoader.loadImg("assets/images/Xbutton.png");
    private static BufferedImage iconBall = ResourceLoader.loadImg("assets/images/iconBall.png");
    private static BufferedImage iconPaddle = ResourceLoader.loadImg("assets/images/iconPaddle.png");
    private static BufferedImage iconGacha = ResourceLoader.loadImg("assets/images/Xbutton.png");
    private final GridPanel gridPanel;
    private final GachaPanel gachaPanel;

    private List<Skins> ballSkins;
    private List<Skins> paddleSkins;
    private int equippedBallId;
    private int equippedPaddleId;

    public ShopScene(InputHandler input, Runnable onBack) {
        super("ShopScene", input);
        this.onBack = onBack;
        this.gridPanel = new GridPanel(input);
        this.gridPanel.setBounds(0,55,Constants.WIDTH,Constants.HEIGHT -60);
        add(gridPanel);
        this.gridPanel.setShopScene(this);

        this.gachaPanel = new GachaPanel(input);
        this.gachaPanel.setShopScene(this);
        this.gachaPanel.setDataChangeListener(this);
        this.gachaPanel.setBounds(0, 55, Constants.WIDTH, Constants.HEIGHT - 60);
        this.gachaPanel.setVisible(false);
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
        this.equippedBallId = ResourceLoader.getEquippedBallId("docs/balls.txt");
        this.equippedPaddleId = ResourceLoader.getEquippedPaddleId("docs/paddles.txt");
        gridPanel.setSkins(ballSkins);
    }

    /** Tạo các button */
    private void initButtons() {
        buttons.add(new IconButton("BACK",iconBack,20,0,50,50,() -> onBack.run()));
        buttons.add(new IconButton("BALLS",iconBall,250,0,50,50,() -> handleButtonClick("BALLS")));
        buttons.add(new IconButton("PADDLES",iconPaddle,350,0,50,50,() -> handleButtonClick("PADDLES")));
        buttons.add(new IconButton("GACHA",iconGacha,450,0,50,50,() -> handleButtonClick("GACHA")));
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
        BufferedImage bar = ResourceLoader.loadImg("assets/images/topBar.png");
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
        int money = ResourceLoader.getMoney("docs/balls.txt");
        g2.drawString(money + "\uD83D\uDCB0",690,30);
    }
    @Override
    protected void render(Graphics2D g2) {
        drawBackground(g2);
        drawMoney(g2);
        drawButtons(g2);

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
}
