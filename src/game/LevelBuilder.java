package game;

import entity.Brick;
import utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    public static List<Brick> buildBricks(LevelData level) {
        List<Brick> bricks = new ArrayList<>();
        int brickWidth = Constants.WIDTH / level.cols;
        int brickHeight = 25;

        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {
                if (level.brickMap[r][c] == 1) {
                    int x = c * brickWidth;
                    int y = 100 + r * brickWidth;
                    bricks.add(new Brick(x, y, brickWidth, brickWidth, 1, Color.RED));
                }
            }
        }
        return bricks;
    }
}
