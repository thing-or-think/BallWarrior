package game;

import core.ResourceLoader;
import javax.sound.sampled.Clip;

public class SoundManager {

    // ĐỊNH NGHĨA TÊN FILE CỦA CÁC ÂM THANH TRONG GAME
    public static final String MUSIC_MENU = "menu_bg.wav";
    public static final String HIT_BRICK = "hit_brick.wav";
    public static final String HIT_PADDLE = "hit_paddle.wav";
    public static final String GAME_OVER = "game_over.wav";
    public static final String GAME_WIN = "win.wav";
    public static final String LOST_HEALTH = "lost_health.wav";
    public static final String EXPLOSION = "explosion.wav";
    public static final String FIZZ = "fizz.wav";
    public static final String SKILL = "skill.wav";
    public static final String ORB_COLLECT = "orb_collect.wav";
    public static final String BALL_SHIELD ="ball_shield.wav";
    public static final String BROKEN = "broken.wav";

    /**
     * Khởi tạo: Tải tất cả âm thanh vào cache khi game khởi động.
     */
    public static void initialize() {
        ResourceLoader.loadSound(MUSIC_MENU);
        ResourceLoader.loadSound(HIT_BRICK);
        ResourceLoader.loadSound(HIT_PADDLE);
        ResourceLoader.loadSound(GAME_OVER);
        ResourceLoader.loadSound(LOST_HEALTH);
        ResourceLoader.loadSound(GAME_WIN);
        ResourceLoader.loadSound(EXPLOSION);
        ResourceLoader.loadSound(FIZZ);
        ResourceLoader.loadSound(SKILL);
        ResourceLoader.loadSound(ORB_COLLECT);
        ResourceLoader.loadSound(BALL_SHIELD);
        ResourceLoader.loadSound(BROKEN);
    }

    /** * Phát âm thanh một lần (Ví dụ: va chạm, kích hoạt kỹ năng).
     * @param soundPath Tên file âm thanh (sử dụng các hằng số bên trên).
     */
    public static void play(String soundPath) {
        Clip clip = ResourceLoader.loadSound(soundPath);
        if (clip != null) {
            // Dừng và Đặt lại vị trí để đảm bảo nó phát từ đầu mỗi lần gọi
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
