package ui.scene;

import core.ResourceLoader;
import core.DataChangeListener;
import data.PlayerData;
import entity.Skins;
import ui.scene.ShopScene;
import ui.base.Button;
import ui.button.BuyButton;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private Skins selectedSkin;

//    private Rectangle buyButtonBounds;
//    private boolean hovered = false;
    private BuyButton buyButton;
    private boolean isBall;
    private DataChangeListener dataChangeListener;
    private ShopScene shopScene;
    private final PlayerData playerData;

    public InfoPanel(PlayerData playerData) {
        this.playerData = playerData;
        setOpaque(false);
        setLayout(null);
        buyButton = new BuyButton("BUY",120,420,160,50,new Font("Serif", Font.BOLD, 24),() -> handleAction());

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                buyButton.setHovered(buyButton.contains(e.getX(),e.getY()));
                repaint();
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (selectedSkin != null && buyButton.contains(e.getX(),e.getY())) {
                    buyButton.onClick();
                }
            }
        });
    }

    public void showSkinInfo(Skins skin) {
        this.selectedSkin = skin;
        updateBuyButtonText();
        repaint();
    }

    public void setDataChangeListener(DataChangeListener listener) {
        this.dataChangeListener = listener;
    }

    private void updateBuyButtonText() {
        if (selectedSkin == null) return;
        String text;
        if (selectedSkin.isBought()) {
            if (shopScene != null) {
                int currentlyEquippedId = isBall
                        ? shopScene.getEquippedBallId()
                        : shopScene.getEquippedPaddleId();
                if (selectedSkin.getId() == currentlyEquippedId) {
                    text = "EQUIPPED";
                } else {
                    text = "EQUIP";
                }
            } else {
                text = "EQUIP";
            }
        } else {
            text = "BUY " + selectedSkin.getPrice() + "$";
        }
        buyButton.setText(text);
    }

    private void handleAction() {
        if (selectedSkin == null) return;
        int purchaseId = selectedSkin.getId();
        // 1. XÁC ĐỊNH LOẠI SKIN VÀ ĐƯỜNG DẪN FILE
        String currentTab = shopScene.getCurrentTab();
        boolean isBall = currentTab.equals("BALLS");
        // Sử dụng file riêng cho Skin và Paddle
        String dataFilePath = isBall ? "docs/balls.txt" : "docs/paddles.txt";
        // Tiền vẫn được lưu trong docs/balls.txt
        String moneyFilePath = "docs/balls.txt";
        // 2. LẤY ID HIỆN TẠI ĐANG TRANG BỊ
        int currentlyEquippedId = isBall
                ? shopScene.getEquippedBallId()
                : shopScene.getEquippedPaddleId();
        if (selectedSkin.isBought()) {
            if (purchaseId != currentlyEquippedId) {
                // 2.1 LƯU ID MỚI VÀO FILE
                if (isBall) {
                    ResourceLoader.setEquippedBallId(dataFilePath, purchaseId);
                } else {
                    ResourceLoader.setEquippedPaddleId(dataFilePath, purchaseId);
                }
                System.out.println("Equipped " + selectedSkin.getName() + " (ID: " + purchaseId + ")");
                // 2.2 THÔNG BÁO TẢI LẠI DỮ LIỆU
                if (dataChangeListener != null) {
                    dataChangeListener.onDataChanged();
                }
            } else {
                System.out.println(selectedSkin.getName() + " đã được trang bị.");
            }
        } else {
            int money = playerData.getCoins();
            int price = selectedSkin.getPrice();
            if (money >= price) {
                money -= price;
                selectedSkin.setBought(true);
                // LƯU TRẠNG THÁI VÀO FILE
                playerData.setCoins(money);
                ResourceLoader.updateIsBought(dataFilePath, purchaseId);
                shopScene.onDataChanged();
                System.out.println("Đã mua " + selectedSkin.getName() + " với giá " + price + " (ID: " + purchaseId + ")");
                // THÔNG BÁO CHO SHOP ĐỂ TẢI LẠI DỮ LIỆU
                if (dataChangeListener != null) {
                    dataChangeListener.onDataChanged();
                }
            } else {
                System.out.println("Không đủ tiền để mua " + selectedSkin.getName());
            }
        }
        updateBuyButtonText();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectedSkin == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // tên skin
        switch (selectedSkin.getRarity()) {
            case COMMON:
                g2.setColor(Color.WHITE);
                break;
            case RARE:
                g2.setColor(Color.CYAN);
                break;
            case EPIC:
                g2.setColor(Color.MAGENTA);
                break;
            case LEGENDARY:
                g2.setColor(Color.ORANGE);
                break;
            default:
                g2.setColor(Color.WHITE);
                break;
        }
        g2.setFont(new Font("Serif", Font.BOLD, 32));
        FontMetrics fm = g2.getFontMetrics();
        String name = selectedSkin.getName().toUpperCase();
        int nameWidth = fm.stringWidth(name);
        g2.drawString(name, (getWidth() - nameWidth) / 2, 80);

        // bóng hoặc paddle
        if (selectedSkin.getImg() != null) {
            if (isBall) {
                g2.drawImage(selectedSkin.getImg(), getWidth() / 2 - 100, 120, 200, 200, null);
            } else {
                g2.drawImage(selectedSkin.getImg(), getWidth() / 2 - 100, 200, 200, 50, null);
            }
        } else {
            g2.setColor(selectedSkin.getColor());
            if (isBall) {
                g2.fillOval(getWidth() / 2 - 80, 140, 160, 160);
            } else {
                g2.fillRect(getWidth() / 2 - 100, 200, 200, 50);
            }
        }
        buyButton.draw(g2);
    }
    public void setIsBall(boolean isBall) {
        this.isBall = isBall;
    }
    public void setShopScene(ShopScene shopScene) {
        this.shopScene = shopScene;
    }
}

