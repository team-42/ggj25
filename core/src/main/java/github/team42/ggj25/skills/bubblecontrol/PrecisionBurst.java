package github.team42.ggj25.skills.bubblecontrol;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.skills.Skill;

public class PrecisionBurst extends Skill {
    public PrecisionBurst() {
        super(0,
            "Precision Burst",
            "This skill increases the projectiles speed, range and damage by 10%!");
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setSpeed(projectile.getSpeed() * 1.2f);
        projectile.setRemainingRange(projectile.getRemainingRange() * 1.2f);
        projectile.setDamage(projectile.getDamage() * 1.2f);
    }
}
