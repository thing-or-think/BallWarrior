package ui.panel;

import core.InputHandler;
import core.ResourceLoader;
import core.SceneManager;
import data.SkinData;
import ui.base.Scene;
import ui.button.BuyButton;
import ui.button.SkinButton;
import ui.scene.OwnedScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GachaPanel extends JPanel {

    private enum State { IDLE, CHEST_OPENNING, SPINNING }
    private State currentState = State.IDLE;

    private final List<SkinData> items;
    private final InputHandler input;
    private final AtomicInteger coins;
    private OwnedScene ownedScene;
    private SceneManager sceneManager;

    private SkinData awardedSkin;
    private final Random rand = new Random();
    private final int COST = 0; //1000
    private BuyButton spinButton;

    // Assets Animation
    private final ImageIcon chestOpeningGif;
    private final BufferedImage chestBeforeImage;
    private final BufferedImage chestAfterImage;

    // Logic Reel
    private final ArrayDeque<SkinData> skinReel = new ArrayDeque<>();
    private final int REEL_SKIN_COUNT = 21;
    private final int CENTER_INDEX = REEL_SKIN_COUNT / 2;
    private final int SKIN_SIZE = 100;
    private final int SKIN_UNIT = SKIN_SIZE + 30;
    private double offset = 0;
    private Timer reelTimer;
    private double currentSpeed = 0;
    private int currentFrame = 0;

    public GachaPanel(InputHandler input,
                      AtomicInteger coins,
                      List<SkinData> items,
                      SceneManager sceneManager) {
        this.input = input;
        this.items = items;
        this.coins = coins;
        this.sceneManager = sceneManager;
        setOpaque(false);
        setDoubleBuffered(true);
        initUI();
        this.chestOpeningGif = new ImageIcon("assets/images/openChest.gif");
        this.chestBeforeImage = ResourceLoader.loadImage("assets/images/gachaBefore.png");
        this.chestAfterImage = ResourceLoader.loadImage("assets/images/gachaAfter.png");
    }

    private void initUI() {
        Font font = new Font("Serif", Font.PLAIN, 32);
        spinButton = new BuyButton("SPIN", Constants.WIDTH / 2 - 50, 450, 100, 50, font, this::handleGachaSpin);
    }

    private SkinData openGacha() {
        int randomIndex = rand.nextInt(items.size());
        return items.get(randomIndex);
    }

    private void generateReel(SkinData finalSkin) {
        skinReel.clear();
        for (int i = 0; i < REEL_SKIN_COUNT; i++) {
            SkinData randomSkin = openGacha();
            if (randomSkin != null) {
                skinReel.offer(randomSkin);
            }
        }
        // Đặt Skin trúng thưởng vào giữa
        List<SkinData> tempList = new ArrayList<>(skinReel);
        if (tempList.size() > CENTER_INDEX) {
            tempList.set(CENTER_INDEX, finalSkin);
            skinReel.clear();
            skinReel.addAll(tempList);
        }
    }

    private void handleGachaSpin() {
        if (currentState != State.IDLE) return;

        if (coins.get() < COST) {
            System.out.println("KHÔNG ĐỦ TIỀN!");
            return;
        }

        // 1. Logic Game: Trừ tiền và chọn Skin
        coins.set(coins.get() - COST);
        awardedSkin = openGacha();

        if (!awardedSkin.isBought()) {
            awardedSkin.setBought(true); // Cập nhật Model
        }

        // 2. Bắt đầu Animation
        generateReel(awardedSkin);
        currentState = State.CHEST_OPENNING;

        if (chestOpeningGif != null) {
            chestOpeningGif.getImage().flush();
        }
        repaint();

        Timer chestTimer = new Timer(1000, e -> {
            startReelAnimation();
        });
        chestTimer.setRepeats(false);
        chestTimer.start();
    }

    private void startReelAnimation() {
        currentState = State.SPINNING;
        offset = 0;

        final double startSpeed = 100.0;
        final double totalDistance = 3.0 * SKIN_UNIT * REEL_SKIN_COUNT - 50;
        final int totalFrames = (int) Math.round(2.0 * totalDistance / startSpeed);
        final double acceleration = - startSpeed / totalFrames;
        currentSpeed = startSpeed;
        currentFrame = 0;

        reelTimer = new Timer(16, e -> {
            currentFrame++;
            if (currentFrame >= totalFrames || currentSpeed <= 0) {
                reelTimer.stop();
                currentState = State.IDLE;
                offset = 0;
                ownedScene.setOwnedAwardedSkin(awardedSkin);
                if (awardedSkin.isBought()) {
                    System.out.println("Trung Skin ... Phai Chiuuuu");
                    ownedScene.setResultMessage("Bạn đã sở hữu skin này");
                } else {
                    System.out.println("Bạn đã nhận được: " + awardedSkin.getName());
                    ownedScene.setResultMessage("Chúc mừng bạn đã nhận được: "+awardedSkin.getName());
                }
                sceneManager.gotoOwned();
                repaint();
                return;
            }
            offset -= currentSpeed;
            currentSpeed += acceleration;

            while (offset < -SKIN_UNIT) {
                offset += SKIN_UNIT;
                skinReel.offer(skinReel.poll());
            }
            repaint();
        });
        reelTimer.start();
    }

    public void update() {
        int mx = input.getMouseX() - getX();
        int my = input.getMouseY() - getY();

        if (currentState == State.IDLE) {
            spinButton.setHovered(spinButton.contains(mx, my));
            if (spinButton.isHovered() && input.consumeClick()) {
                spinButton.onClick();
            }
        } else {
            spinButton.setHovered(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentState == State.IDLE && spinButton != null) {
            spinButton.draw(g2);
        }

        switch (currentState) {
            case IDLE:
                if (true) {
                    int chestWidth = 300, chestHeight = 300;
                    int x = getWidth() / 2 - chestWidth / 2;
                    int y = getHeight() / 2 - chestHeight / 2;
                    if (chestBeforeImage != null) {
                        g2.drawImage(chestBeforeImage, x, y, chestWidth, chestHeight, null);
                    }
                }
                break;

            case CHEST_OPENNING:
                if (chestOpeningGif != null) {
                    int chestWidth = 300, chestHeight = 300;
                    int x = getWidth() / 2 - chestWidth / 2;
                    int y = getHeight() / 2 - chestHeight / 2;
                    g2.drawImage(chestOpeningGif.getImage(), x, y, chestWidth, chestHeight, this);
                }
                break;

            case SPINNING:
                if (chestAfterImage != null) {
                    int chestWidth = 300, chestHeight = 300;
                    int x = getWidth() / 2 - chestWidth / 2;
                    int y = getHeight() / 2 - chestHeight / 2;
                    g2.drawImage(chestAfterImage, x, y, chestWidth, chestHeight, null);
                }
                g2.setColor(new Color(0,0,0,150));
                g2.fillRect(0,0,getWidth(),140);
                g2.fillRect(0,300,getWidth(),300);

                paintReel(g2);
                break;
        }
        g2.dispose();
    }

    private void paintReel(Graphics2D g2) {
        int panelWidth = getWidth();
        int centerX = panelWidth / 2;
        int drawY = getHeight() / 2 - 50;

        List<SkinData> skinsToDraw = new ArrayList<>(skinReel);

        for (int i = 0; i < skinsToDraw.size(); i++) {
            double skinX = centerX - (CENTER_INDEX * SKIN_UNIT) + (i * SKIN_UNIT) + offset;
            if (skinX < -SKIN_UNIT || skinX > panelWidth + SKIN_UNIT) continue;

            SkinData skinData = skinsToDraw.get(i);

            SkinButton tempButton = new SkinButton(
                    (int) (skinX - SKIN_SIZE / 2.0),
                    drawY - SKIN_SIZE / 2,
                    SKIN_SIZE,
                    SKIN_SIZE,
                    skinData
            );
            tempButton.draw(g2);
        }

        // Vẽ marker chính giữa
        g2.setColor(new Color(255, 255, 255, 120));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(centerX - SKIN_UNIT / 2, drawY - SKIN_SIZE / 2 - 10, SKIN_UNIT, SKIN_SIZE + 20, 15, 15);
    }

    public void setOwnedScene(OwnedScene ownedScene) {
        this.ownedScene = ownedScene;
    }
}
