package github.team42.ggj25.skills;

import github.team42.ggj25.skills.bubblecontrol.BubbleControl;
import github.team42.ggj25.skills.bubbledensity.BubbleDensity;

public enum SkillTrees {
    BUBBLE_CONTROL(BubbleControl.values()),
    BUBBLE_DENSITY(BubbleDensity.values()),

    ;

    private final Skilltree[] skilltreeLevels;

    SkillTrees(Skilltree[] skilltreeLevels) {
        this.skilltreeLevels = skilltreeLevels;
    }

    public Skill getSkillByLevel(int level) {
        if (level < 0 || level >= skilltreeLevels.length) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        return skilltreeLevels[level].getSkill();
    }
}
