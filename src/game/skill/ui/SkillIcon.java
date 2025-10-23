package game.skill.ui;

import game.skill.base.ActiveSkill;

import java.awt.*;

public class SkillIcon {
    private ActiveSkill skill;
    private int x;
    private int y;
    private int size;

    public SkillIcon(ActiveSkill skill, int x, int y, int size) {
        this.skill = skill;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics2D g) {
        // Vẽ khung nền
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(x, y, size, size, 8, 8);

        // Vẽ icon (nếu có)
        Image icon = skill.getIcon();
        if (icon != null) {
            g.drawImage(icon, x, y, size, size, null);
        } else {
            // Icon tạm (nếu skill chưa có hình)
            g.setColor(Color.GRAY);
            g.fillRect(x, y, size, size);
            g.setColor(Color.WHITE);
            g.drawString(skill.getName(), x + 6, y + size / 2);
        }

        if (!skill.isReady()) {
            float progress = skill.getCooldownProgress(); // 1.0 → full cooldown
            int h = (int) (size * progress);
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(x, y + (size - h), size, h);

            g.setColor(Color.black);
            String timeText = String.format("%.1fs", skill.getCooldownTimer());
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(timeText);
            g.drawString(timeText, x + (size - tw) / 2, y + size / 2 + 5);
        }

//        if (skill.isActive()) {
//            g.setColor(Color.CYAN);
//            g.setStroke(new BasicStroke(3));
//            g.drawRoundRect(x - 2, y - 2, size + 4, size + 4, 10, 10);
//        } else {
//            g.setColor(Color.BLACK);
//            g.drawRoundRect(x, y, size, size, 8, 8);
//        }
    }

}
