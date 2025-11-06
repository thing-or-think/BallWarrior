package ui.scene;

import core.AudioService;
import core.InputHandler;
import core.SceneManager;
import data.SkinData;
import ui.base.Scene;
import ui.element.Label;
import ui.button.SkinButton;

import javax.swing.*;
import java.awt.*;

public class OwnedScene extends Scene {

    private Label resultMessage;
    private final SceneManager sceneManager;
    private SkinData awardedSkin;

    private final int SKIN_SIZE = 150;

    public OwnedScene(InputHandler input, SceneManager sceneManager) {
        super("OwnedScene", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    public void initUI() {
        this.background = new ImageIcon("assets/images/Bg/ownedscene.gif");
        this.resultMessage = new Label("...", 400, 600, new Font("Monospaced", Font.BOLD, 30), Color.WHITE);
    }

    @Override
    public void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            AudioService.playSound("music.wav");
            sceneManager.goToShop();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (background != null) {
            g2.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        resultMessage.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString("---Press ESC to continue---", 390, 670);

        if (awardedSkin != null) {
            int x = getWidth() / 2 - SKIN_SIZE / 2;
            int y = getHeight() / 2 - SKIN_SIZE / 2;

            SkinButton.simpleDraw(g2,awardedSkin,x,y,SKIN_SIZE,SKIN_SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 35));
            int textWidth = g2.getFontMetrics().stringWidth(awardedSkin.getName());
            int xPos = getWidth() / 2 - textWidth / 2;
            g2.drawString(awardedSkin.getName(), xPos, 50);
        }
    }

    /** Setter mới, nhận SkinData */
    public void setOwnedAwardedSkin(SkinData awardedSkin) {
        this.awardedSkin = awardedSkin;
    }

    /** Setter */
    public void setResultMessage(String message) {
        this.resultMessage.setText(message);
    }

}
