package game;

import entity.Brick;
import utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

    public static List<Brick> buildBricks(LevelData level) {
        List<Brick> bricks = new ArrayList<>();

        // ====== 1) KHU VỰC ĐỂ BUILD MAP ======
        int marginTop = 40;
        int marginSide = 40;
        int availableWidth  = Constants.GAME_PANEL_WIDTH  - marginSide * 2;
        int availableHeight = Constants.GAME_PANEL_HEIGHT - marginTop  - 120; // chừa chỗ đá bóng

        // ====== 2) TÍNH KÍCH THƯỚC GẠCH THEO MÀN ======
        int brickW = availableWidth  / level.cols;
        int brickH = availableHeight / level.rows;

        // Lấy kích thước vuông và vừa nhất
        int brickSize = Math.min(brickW, brickH);

        // ====== 3) CĂN GIỮA TOÀN BỘ MAP ======
        int totalW = brickSize * level.cols;
        int totalH = brickSize * level.rows;

        int startX = (Constants.GAME_PANEL_WIDTH  - totalW) / 2;
        int startY = marginTop;

        // ====== 4) TẠO GẠCH ======
        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {

                Brick.Type type;
                switch (level.brickMap[r][c]) {
                    case 0: type = Brick.Type.BEDROCK; break;
                    case 1: type = Brick.Type.COBBLESTONE; break;
                    case 2: type = Brick.Type.GOLD; break;
                    default: type = Brick.Type.DIAMOND; break;
                }

                bricks.add(new Brick(
                        startX + c * brickSize,
                        startY + r * brickSize,
                        brickSize,
                        brickSize,
                        type
                ));
            }
        }

        return bricks;
    }
}
