package github.team42.ggj25.skills.bubblecontrol;

import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.Skilltree;

public enum BubbleControl implements Skilltree {
    SWIFT_PROJECTILE(new SwiftProjectile()),
    LONG_SHOT(new LongShot()),
    PRECISION_BURST(new PrecisionBurst()),
    SONIC_BUBBLE(new SonicBubble()),
    INFINITE_HORIZON(null),
    ;


    private final Skill skill;

    BubbleControl(Skill skill) {
        this.skill = skill;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }
}
