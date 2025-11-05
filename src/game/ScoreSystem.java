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
        score += points * (int) combo;
    }

    public int getScore() {
        return score;
    }

    public void increaseCombo(float amount) {
        combo = Math.min(combo + amount, 3f);
    }

    public void resetCombo() {
        combo = 1f;
    }

    public float getCombo() {
        return combo;
    }

    public void addMana(int amount) {
        this.mana += amount;
    }

    public int getMana() {
        return mana;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        if(lives <= 0) {
            return true;
        }
        return false;
    }
}
