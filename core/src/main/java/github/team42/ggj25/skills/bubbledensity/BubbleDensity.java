package github.team42.ggj25.skills.bubbledensity;

import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.Skilltree;

public enum BubbleDensity implements Skilltree {
    RAPID_FIRE(new RapidFire()),
    ENLARGED_BUBBLE(new EnlargedBubble()),
    DOUBLE_VOLLEY(new DoubleVolley()),
    BUBBLE_STREAM(new BubbleStream()),
    BUBBLE_BARRAGE(null),
    ;

    private final Skill skill;

    BubbleDensity(Skill skill) {
        this.skill = skill;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }
}
