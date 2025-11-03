package ui.scene;

import core.InputHandler;
import core.SceneManager;
import ui.base.AnchorType;
import ui.base.Scene;
import ui.element.Label;
import utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InstructionScene extends Scene {

    private final SceneManager sceneManager;
    private final List<Label> componentsToDraw = new ArrayList<>(); // Dùng List để quản lý các Labels

    public InstructionScene(InputHandler input, SceneManager sceneManager) {
        super("InstructionScene", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    public void initUI() {
        setBackground(Color.DARK_GRAY);
        setLayout(null);

        // --- 1. TIÊU ĐỀ ---
        Label title = new Label(
                "CÁCH CHƠI (INSTRUCTIONS)",
                Constants.WINDOW_WIDTH/ 2,
                50,
                new Font("Serif", Font.BOLD, 40),
                AnchorType.CENTER_TOP
        );
        componentsToDraw.add(title); // <-- THÊM VÀO LIST

        // --- 2. NỘI DUNG HƯỚNG DẪN ---
        Font contentFont = new Font("Monospaced", Font.PLAIN, 18);
        Color textColor = Color.WHITE;
        int startY = 150;
        int lineHeight = 30;

        String[] instructions = {
                "1. Di chuyển thanh đỡ (Paddle) bằng phím mũi tên LEFT/RIGHT.",
                "2. Mục tiêu là không để bóng (Ball) lọt qua phía sau thanh đỡ.",
                "3. 6",
                "4. Chạm bóng vào gạch để phá vỡ chúng và ghi điểm.",
                "5. Sử dụng tiền (Coins) để mở khóa Skin mới trong Shop.",
                "",
                "NHẤN PHÍM ESC HOẶC ENTER ĐỂ TIẾP TỤC"
        };

        for (int i = 0; i < instructions.length; i++) {
            Label line = new Label(
                    instructions[i],
                    Constants.WINDOW_WIDTH / 2,
                    startY + i * lineHeight,
                    contentFont,
                    AnchorType.CENTER_TOP
            );
            componentsToDraw.add(line); // <-- THÊM VÀO LIST
        }
    }

    @Override
    public void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ENTER)) {
            sceneManager.goToMenu();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        // VẼ BACKGROUND MÀU ĐƠN SẮC
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // VẼ TẤT CẢ COMPONENTS MỘT CÁCH THỦ CÔNG
        for (Label label : componentsToDraw) {
            label.draw(g2);
        }
    }
}
