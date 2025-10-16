package game;

/**
 * ScoreSystem quản lý điểm số và số mạng của người chơi trong game.
 * - Điểm (score) sẽ tăng khi người chơi phá gạch.
 * - Mạng (lives) sẽ giảm khi bóng rơi xuống ngoài paddle.
 */
public class ScoreSystem {

    private int score; // điểm hiện tại
    private int lives; // số mạng còn lại
    private float combo; //combo

    /**
     * Constructor mặc định.
     * Gọi reset() để thiết lập lại điểm và mạng ban đầu.
     */
    public ScoreSystem() {
        reset();
    }

    /**
     * Đặt lại trạng thái ban đầu:
     * - Điểm = 0
     * - Mạng = 3 (mặc định)
     * - Combo = 1
     */
    public void reset() {
        this.score = 0;
        this.lives = 3; // mặc định 3 mạng
        this.combo = 1f; //mặc định 1
    }

    // --- Quản lý điểm ---

    /**
     * Cộng thêm điểm khi người chơi ghi điểm.
     * @param points số điểm cộng thêm
     */
    public void addScore(int points) {
        score += points * combo;
    }

    /**
     * Lấy điểm hiện tại.
     */
    public int getScore() {
        return score;
    }

    // --- Quản lý combo ---

    // cập nhật combo
    public void increaseCombo(float amount) {
        combo = Math.min(combo + amount, 3f);
    }

    public void resetCombo() {
        combo = 1f;
    }

    public float getCombo() {
        return combo;
    }

    // --- Quản lý mạng ---

    /**
     * Mất 1 mạng (ví dụ khi bóng rơi xuống ngoài paddle).
     * Nếu còn mạng thì giảm, nếu đã = 0 thì không giảm nữa.
     */
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    /**
     * Thêm 1 mạng (ví dụ khi ăn được item bonus).
     */
    public void addLife() {
        lives++;
    }

    /**
     * Lấy số mạng hiện tại.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Kiểm tra game over (hết mạng).
     * @return true nếu số mạng <= 0
     */
    public boolean isGameOver() {
        if(lives <= 0) {
            lives = 3;
            return true;
        }
        return false;
    }
}
