package github.team42.ggj25.skills.bubblecontrol;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.skills.Skill;

public class SonicBubble extends Skill {
    public SonicBubble() {
        super(0,
            "Sonic Bubble",
            "This skill doubles the projectile range!");
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setRemainingRange(projectile.getRemainingRange() * 2.0f);
    }
}
