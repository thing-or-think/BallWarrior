package game;

public class ScoreSystem {

    private int score;
    private int lives;
    private float combo;
    private int mana;

    public ScoreSystem() {
        reset();
    }

    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.combo = 1f;
        this.mana = 0;
    }

    // Score
    public void addScore(int points) {
        score += points * combo;
    }

    public int getScore() {
        return score;
    }

    // Combo
    public void increaseCombo(float amount) {
        combo = Math.min(combo + amount, 3f);
    }

    public void resetCombo() {
        combo = 1f;
    }

    public float getCombo() {
        return combo;
    }

    // Mana
    public void addMana(int amount) {
        this.mana += amount;
    }

    public int getMana() {
        return mana;
    }

    // Lives
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void addLife() {
        lives++;
    }

    public int getLives() {
        return lives;
    }

    // State
    public boolean isGameOver() {
        return lives <= 0;
    }
}
