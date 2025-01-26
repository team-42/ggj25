package github.team42.ggj25.skills.bubbledensity;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.entity.Weapon;
import github.team42.ggj25.skills.Skill;

public class BubbleStream extends Skill {
    public BubbleStream() {
        super(0,
            "Bubble Stream",
            "Increases the weapons fire rate by 30% at the cost of 10% projectile range!");
    }

    @Override
    public boolean manipulateWeapon(Weapon weapon) {
        weapon.setFireRate(weapon.getFireRate() * 0.7f);

        return true;
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setRemainingRange(projectile.getRemainingRange() * 0.9f);
    }
}
