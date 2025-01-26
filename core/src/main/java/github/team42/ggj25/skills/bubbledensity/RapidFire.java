package github.team42.ggj25.skills.bubbledensity;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.entity.Weapon;
import github.team42.ggj25.skills.Skill;

public class RapidFire extends Skill {
    public RapidFire() {
        super(0,
            "Rapid Fire",
            "This skill increases the weapons fire rate by 30%!");
    }

    @Override
    public boolean manipulateWeapon(Weapon weapon) {
        weapon.setFireRate(weapon.getFireRate() * 0.7f);
        return true;
    }
}
