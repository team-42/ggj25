package github.team42.ggj25.skills.bubbledensity;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.entity.Weapon;
import github.team42.ggj25.skills.Skill;

public class DoubleVolley extends Skill {
    public DoubleVolley() {
        super(0,
            "Double Volley",
            "This skill increases the weapons fire rate by 50%, at the cost of 10% range!");
    }

    @Override
    public boolean manipulateWeapon(Weapon weapon) {
        weapon.setFireRate(weapon.getFireRate() * 1.5f);

        return true;
    }

    @Override
    public void manipulateProjectile(Projectile projectile) {
        projectile.setRemainingRange(projectile.getRemainingRange() * 0.9f);
    }
}
