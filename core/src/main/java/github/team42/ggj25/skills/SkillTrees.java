package github.team42.ggj25.skills;

import github.team42.ggj25.skills.bubblecontrol.BubbleControl;
import github.team42.ggj25.skills.bubbledensity.BubbleDensity;

public enum SkillTrees {
    NO_SKILL(null, "no_skill.png", ""),
    BUBBLE_CONTROL(BubbleControl.values(), "bubble_control.png", "bubble_control_ulti.png"),
    BUBBLE_DENSITY(BubbleDensity.values(), "bubble_density.png", "bubble_density_ulti.png"),

    ;

    private final Skilltree[] skilltreeLevels;
    private final String pathToIcon;
    private final String pathToUltiIcon;

    SkillTrees(Skilltree[] skilltreeLevels, String pathToIcon, String pathToUltiIcon) {
        this.skilltreeLevels = skilltreeLevels;
        this.pathToIcon = pathToIcon;
        this.pathToUltiIcon = pathToUltiIcon;
    }

    public Skill getSkillByLevel(int level) {
        if (level < 0 || level >= skilltreeLevels.length) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        return skilltreeLevels[level].getSkill();
    }

    public String getPathToIcon() {
        return pathToIcon;
    }

    public String getPathToUltiIcon() {
        return pathToUltiIcon;
    }
}
