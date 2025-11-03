package game;

import entity.Brick;
import utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

    public static List<Brick> buildBricks(LevelData level) {

        List<Brick> bricks = new ArrayList<>();
        int brickSize = 50;

        // Tọa độ gạch sẽ được tính toán ĐỐI VỚI GÓC (0, 0) CỦA GAME PANEL.
        // GamePanel sẽ tự dịch chuyển khối này lên Window.

        // 1. VỊ TRÍ X
        // Tổng chiều rộng map gạch
        int totalWidth = level.cols * brickSize;

        // Căn giữa dãy gạch theo chiều ngang của Game Panel (tọa độ X bắt đầu)
        int startX = (Constants.GAME_PANEL_WIDTH - totalWidth) / 2;

        // 2. VỊ TRÍ Y (SÁT LỀ TRÊN GAME PANEL)
        // Gạch bắt đầu ngay tại lề trên của Game Panel (y = 0).
        int startY = 0;

        // 3. XÂY DỰNG GẠCH VỚI TỌA ĐỘ BÊN TRONG GAME PANEL
        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {

                int cell = level.brickMap[r][c];
                if (cell < 0) continue;

                Brick.Type type;
                switch (cell) {
                    case 0: type = Brick.Type.BEDROCK; break;
                    case 1: type = Brick.Type.COBBLESTONE; break;
                    case 2: type = Brick.Type.GOLD; break;
                    case 3: default: type = Brick.Type.DIAMOND; break;
                }

                // Tọa độ X cuối cùng: Vị trí bắt đầu + Offset cột
                int finalX = startX + c * brickSize;

                // Tọa độ Y cuối cùng: Vị trí bắt đầu + Offset hàng
                int finalY = startY + r * brickSize;

                bricks.add(new Brick(
                        finalX,
                        finalY,
                        brickSize,
                        brickSize,
                        type
                ));
            }
        }

        return bricks;
    }
}
