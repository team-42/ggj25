package github.team42.ggj25.skills.bubbledensity;

import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.entity.Weapon;
import github.team42.ggj25.skills.Skill;

public class EnlargedBubble extends Skill {
    public EnlargedBubble() {
        super(0,
            "Enlarged Bubble",
            "This skill enlarges the projectile by 50%!");
    }

    @Override
    public boolean manipulateWeapon(Weapon weapon) {
        weapon.setProjectileHeight(weapon.getProjectileHeight() * 1.5f);
        weapon.setProjectileWidth(weapon.getProjectileWidth() * 1.5f);

        return true;
    }
}
