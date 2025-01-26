package github.team42.ggj25.skills.bubblecontrol;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.skills.Skill;

/**
 * Tree: Bubble Control
 * Level 1: Swift Projetile
 * increase projectile speed by 20%.
 */
public class SwiftProjectile extends Skill {
    public SwiftProjectile() {
        super(0,
            "Swift Projectile",
            "This skill increases the projectile speed by 20%!");
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setSpeed(projectile.getSpeed() * 1.4f);
    }
}
