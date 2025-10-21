package ui.panel;

import core.DataChangeListener;
import core.InputHandler;
import core.ResourceLoader;
import data.PlayerData;
import entity.Skins;
import ui.base.Button;
import ui.button.BuyButton;
import ui.scene.ShopScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class GachaPanel extends JPanel {
    private List<Skins> skinsList;
    private ShopScene shopScene;
    private DataChangeListener dataChangeListener;
    private InputHandler input;
    private BufferedImage background;
    private Skins awardedSkin;
    private Random rand = new Random();
    private final int gachaCost = 1000;
    private Button spinButton;
    private boolean hovered = false;
    private final PlayerData playerData;

    public GachaPanel(InputHandler input, PlayerData playerData) {
        this.input = input;
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        this.background = ResourceLoader.loadImage("assets/images/shopBg.jpg");
        this.playerData = playerData;
        initUI();
        initMouse();
    }

    private void initUI() {
        Font font = new Font("Serif", Font.PLAIN, 32);
        spinButton = new BuyButton("SPIN", Constants.WIDTH/2-50,400,100,50,font,() -> handleGachaSpin());
    }

    private void initMouse() {
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                spinButton.setHovered(spinButton.contains(e.getX(),e.getY()));
                repaint();
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (spinButton.contains(e.getX(),e.getY())) {
                    spinButton.onClick();
                }
            }
        });
    }

    private Skins openGacha() {
        List<Skins> ballSkins = shopScene.getBallSkins();
        List<Skins> paddleSkins = shopScene.getPaddleSkins();
        int typeChoice = rand.nextInt(2);
        List<Skins> chosenList;
        if (typeChoice == 0) {
            chosenList = ballSkins;
        } else {
            chosenList = paddleSkins;
        }
        if (chosenList == null || chosenList.isEmpty()) {
            chosenList = (typeChoice == 0) ? paddleSkins : ballSkins;
            if (chosenList == null || chosenList.isEmpty()) {
                System.out.println("❌ Lỗi: Không có Skin nào trong kho.");
                return null;
            }
        }
        int randomIndex = rand.nextInt(chosenList.size());
        return chosenList.get(randomIndex);
    }
    private void handleGachaSpin() {
        //if (shopScene == null) return;
        int money = playerData.getCoins().get();
        if (money < gachaCost) {
            System.out.println("KHÔNG ĐỦ TIỀN!");
            return;
        }
        money -= gachaCost;
        playerData.setCoins(money);

        awardedSkin = openGacha();
        if (awardedSkin == null) return;
        int purchaseId = awardedSkin.getId();
        String awardedName = awardedSkin.getName();

        boolean isBall = shopScene.getBallSkins().contains(awardedSkin);
        String dataFilePath = isBall ? "docs/balls.txt" : "docs/paddles.txt";

        if (!awardedSkin.isBought()) {
            awardedSkin.setBought(true);
            ResourceLoader.updateIsBought(dataFilePath, purchaseId);
            shopScene.onDataChanged();
            System.out.println("🎉 CHÚC MỪNG! Bạn nhận được Skin MỚI: " + awardedName);
        } else {
            System.out.println("✨ TRÙNG SKIN: " + awardedName + " phải CHỊU");
        }
        dataChangeListener.onDataChanged();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background,0,0,getWidth(),getHeight(),null);
        if (awardedSkin != null) {
            int skinSize = 250;
            int x = getWidth() / 2 - skinSize / 2;
            int y = getHeight() / 2 - skinSize / 2;


        }
        spinButton.draw(g2);
    }

    public void setShopScene(ShopScene shopScene) {
        this.shopScene = shopScene;
    }

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }
}
