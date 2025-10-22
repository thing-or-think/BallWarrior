package ui.element;

import core.ResourceLoader;
import java.awt.image.BufferedImage;
import java.awt.*;

public class HealthBar {

    private final BufferedImage[] frames;
    private final int maxHealth;
    private int currentHealth;

    // --- BIẾN MỚI CHO HIỆU ỨNG NHÁY ---
    private long flashTimer = 0;
    private boolean isFlashing = false;
    private int flashingOldHealth; // Lưu trữ máu trước khi nhận sát thương

    // Đảm bảo FLASH_DURATION_MS > FLASH_RATE_MS để có ít nhất 1 lần nháy
    private static final long FLASH_DURATION_MS = 800; // Nháy trong 0.8 giây
    private static final long FLASH_RATE_MS = 100;    // Tần suất chuyển đổi frame (100ms/frame)

    // Kích thước cố định cho hiển thị (giữ nguyên)
    private static final int DISPLAY_WIDTH = 144;
    private static final int DISPLAY_HEIGHT = 36;

    // Các đường dẫn ảnh (giữ nguyên)
    private static final String[] FRAME_PATHS = {
            "assets/images/HUB/heal_0_0.png", // [0]: Full 1 (Máu 3, Cột 1)
            "assets/images/HUB/heal_0_1.png", // [1]: Full 2 (Máu 3, Cột 2)
            "assets/images/HUB/heal_1_0.png", // [2]: Medium 1 (Máu 2, Cột 1)
            "assets/images/HUB/heal_1_1.png", // [3]: Medium 2 (Máu 2, Cột 2)
            "assets/images/HUB/heal_2_0.png", // [4]: Low 1 (Máu 1, Cột 1)
            "assets/images/HUB/heal_2_1.png"  // [5]: Low 2 (Máu 1, Cột 2)
    };

    public HealthBar(int maxHealth, String imageBaseDir) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.flashingOldHealth = maxHealth;

        // ... (Logic tải 6 ảnh giữ nguyên) ...
        BufferedImage[] loadedFrames = new BufferedImage[6];
        int loadedCount = 0;
        for (int i = 0; i < 6; i++) {
            BufferedImage img = ResourceLoader.loadImg(FRAME_PATHS[i]);
            if (img != null) {
                loadedFrames[i] = img;
                loadedCount++;
            } else {
                System.err.println("❌ Lỗi tải ảnh: " + FRAME_PATHS[i]);
            }
        }

        if (loadedCount < 6) {
            this.frames = new BufferedImage[0];
        } else {
            this.frames = loadedFrames;
        }
    }

    /**
     * Cập nhật máu và kích hoạt trạng thái nháy giữa máu cũ và máu mới.
     */
    public void setCurrentHealth(int health) {
        int newHealth = Math.max(0, health);

        // Kích hoạt nháy nếu máu bị mất (và không phải máu đầy)
        if (newHealth < this.currentHealth) {
            // Chỉ bắt đầu nháy nếu máu thật sự giảm
            this.isFlashing = true;
            this.flashTimer = System.currentTimeMillis() + FLASH_DURATION_MS;
            this.flashingOldHealth = this.currentHealth; // LƯU MÁU CŨ TRƯỚC KHI CẬP NHẬT
        }

        this.currentHealth = newHealth;
    }

    // Phương thức Helper: Lấy Base Index (Cột 1) của trạng thái máu
    private int getBaseIndexForHealth(int health) {
        return switch (health) {
            case 3 -> 0; // Máu 3: Index 0
            case 2 -> 2; // Máu 2: Index 2
            case 1 -> 4; // Máu 1: Index 4
            default -> -1; // Máu 0 hoặc lỗi
        };
    }

    public void draw(Graphics2D g, int x, int y) {
        if (frames.length < 6 || currentHealth <= 0) return;

        // 1. CHỌN BASE INDEX cho trạng thái máu hiện tại
        int newBaseIndex = getBaseIndexForHealth(currentHealth);
        int finalIndex = newBaseIndex;

        // 2. XỬ LÝ TRẠNG THÁI NHÁY (FLASHING)

        // Tắt nháy nếu hết thời gian
        if (isFlashing && System.currentTimeMillis() > flashTimer) {
            isFlashing = false;
        }

        // Nếu đang trong trạng thái nháy
        if (isFlashing) {
            int oldBaseIndex = getBaseIndexForHealth(flashingOldHealth);

            // Lấy frameToggle luân phiên giữa 0 và 1 (Máu Mới và Máu Cũ)
            long frameToggle = (System.currentTimeMillis() / FLASH_RATE_MS) % 2;

            if (frameToggle == 0) {
                // Hiển thị trạng thái MÁU MỚI (Vẫn dùng Cột 1 cho đơn giản, không nháy)
                finalIndex = newBaseIndex;
            } else {
                // Hiển thị trạng thái MÁU CŨ
                finalIndex = oldBaseIndex;
            }

            // Xử lý trường hợp nháy với máu đầy (máu cũ = 3)
            // Nếu máu cũ là 3, ta có thể dùng frame [1] (Full 2) để tạo hiệu ứng flash
            if (finalIndex == 0 && frameToggle == 1) {
                finalIndex = 1;
            }

        } else {
            // Không nháy: Luôn chọn frame Cột 1 của trạng thái hiện tại
            finalIndex = newBaseIndex;
        }

        // Kiểm tra an toàn
        if (finalIndex < 0 || finalIndex >= frames.length) {
            finalIndex = 0; // Quay về frame Full 1 nếu lỗi
        }

        BufferedImage frameToDraw = frames[finalIndex];

        // 3. VẼ VỚI KÍCH THƯỚC PHÓNG TO
        g.drawImage(frameToDraw, x, y, DISPLAY_WIDTH, DISPLAY_HEIGHT, null);
    }
}
