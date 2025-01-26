package github.team42.ggj25.skills.bubblecontrol;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.skills.Skill;

/**
 * Tree: Bubble Control
 * Level 2: Long Shot
 * increase projectile range by 25%.
 */
public class LongShot extends Skill {
    public LongShot() {
        super(0,
            "Long Shot",
            "This skill increases the projectiles range by 25%!");
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setRemainingRange(projectile.getRemainingRange() * 1.5f);
    }
}
