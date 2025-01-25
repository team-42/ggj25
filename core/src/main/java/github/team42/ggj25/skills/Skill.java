package github.team42.ggj25.skills;

import github.team42.ggj25.entity.Frog;
import github.team42.ggj25.entity.Projectile;

public abstract class Skill {
    public int cost;
    public String skillName;
    public String descriptionText;

    public Skill () {

    }

    public Skill (int cost, String skillName, String descriptionText) {
        this.cost = cost;
        this.skillName = skillName;
        this.descriptionText = descriptionText;
    }

    public void manipulateFrog(Frog frog) {

    }

    public void manipulateProjectile(Projectile projectile) {

    }
}
