package game.skill.ui;

import game.skill.SkillManager;
import game.skill.base.ActiveSkill;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkillPanel {
    private SkillManager skillManager;
    private List<SkillIcon> icons;
    private int startX;
    private int startY;
    private int iconSize;
    private int spacing;

    public SkillPanel(SkillManager skillManager, int startX, int startY) {
        this.skillManager = skillManager;
        this.startX = startX;
        this.startY = startY;
        this.iconSize = 48;
        this.spacing = 8;

        icons = new ArrayList<>();
        rebuildIcons();
    }

    public void rebuildIcons() {
        icons.clear();
        int x = startX;
        for (ActiveSkill skill : skillManager.getActiveSkills()) {
            icons.add(new SkillIcon(skill, x, startY, iconSize));
            x += iconSize + spacing;
        }
    }

    public void draw(Graphics2D g) {
        for (SkillIcon icon : icons) {
            icon.draw(g);
        }
    }
}
