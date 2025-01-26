package github.team42.ggj25.skills;

import github.team42.ggj25.entity.Frog;
import github.team42.ggj25.entity.Projectile;
import github.team42.ggj25.entity.Weapon;

public abstract class Skill {
    public int cost;
    public String skillName;
    public String descriptionText;

    public Skill() {

    }

    public Skill(int cost, String skillName, String descriptionText) {
        this.cost = cost;
        this.skillName = skillName;
        this.descriptionText = descriptionText;
    }

    public boolean manipulateFrog(Frog frog) {
        return false;
    }

    public void manipulateProjectile(Projectile projectile) {

    }

    public void manipulatesScore() {

    }

    /**
     * @param weapon
     * @return false if skill has no weapon manipulation, true otherwise
     */
    public boolean manipulateWeapon(Weapon weapon) {
        return false;
    }
}
