package ui.scene;

import core.InputHandler;
import core.SceneManager;
import core.ResourceLoader; // Vẫn cần để load background image
import data.SkinData; // <-- Dùng Data Model mới
import ui.base.Scene;
import ui.element.Label;
import ui.button.SkinButton; // <-- Dùng để vẽ SkinData

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class OwnedScene extends Scene {

    private Label resultMessage;
    private final SceneManager sceneManager;
    private SkinData awardedSkin; // <-- Đã thay thế Skins bằng SkinData

    // Assets cho hiển thị skin
    private final int SKIN_SIZE = 150;
    private SkinButton awardedSkinRenderer; // <-- Renderer mới

    public OwnedScene(InputHandler input, SceneManager sceneManager) {
        super("OwnedScene", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    public void initUI() {
        this.background = new ImageIcon("assets/images/ownedscene.gif");
        this.resultMessage = new Label("...", 200, 500, new Font("Monospaced", Font.BOLD, 30), Color.WHITE);
        // Khởi tạo renderer rỗng
        this.awardedSkinRenderer = new SkinButton(0, 0, SKIN_SIZE, SKIN_SIZE, null);
    }

    @Override
    public void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            //sceneManager.setReturnFromGacha(true);
            sceneManager.goToShop();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        // Vẽ background (nếu background không phải là GIF, cần phải tự vẽ Image)
        if (background != null) {
            g2.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        resultMessage.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString("---Press ESC to continue---", 180, 570);

        if (awardedSkin != null) {
            // 1. VẼ SKIN (Dùng SkinButton Renderer mới)
            int x = getWidth() / 2 - SKIN_SIZE / 2;
            int y = 300 - SKIN_SIZE / 2;

            // Cập nhật vị trí và Data cho Renderer
            awardedSkinRenderer.setBound(new Rectangle(x, y, SKIN_SIZE, SKIN_SIZE));

            // Vẽ Skin (vì đây là màn hình thông báo, ta không cần hiển thị giá hay "EQUIPPED")
            // SkinButton.draw(g2) sẽ vẽ nền rarity và hình ảnh/màu sắc của skin.
            awardedSkinRenderer.draw(g2);

            // 2. VẼ TÊN SKIN (Giữ nguyên logic cũ)
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
        // Cập nhật Renderer để vẽ skin mới
        if (this.awardedSkinRenderer != null) {
            this.awardedSkinRenderer = new SkinButton(0, 0, SKIN_SIZE, SKIN_SIZE, awardedSkin);
        }
    }

    /** Setter giữ nguyên */
    public void setResultMessage(String message) {
        this.resultMessage.setText(message);
    }

}
