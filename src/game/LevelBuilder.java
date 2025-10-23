package game;

import entity.Brick;
import utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    public static List<Brick> buildBricks(LevelData level) {
        List<Brick> bricks = new ArrayList<>();
        int brickWidth = Constants.GAME_PANEL_WIDTH / level.cols;
        int brickHeight = 25;

        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {
                if (level.brickMap[r][c] == 1) {
                    int x = c * brickWidth;
                    int y = 50 + r * brickHeight;
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, Color.RED));
                }
            }
        }
        return bricks;
    }
}
