package github.team42.ggj25.skills.InterestControl;

import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.Skilltree;

public enum InterestRate implements Skilltree {
    INCREASE_INTEREST(new IncreaseInterest());

    private final Skill skill;

    InterestRate(Skill skill) {
        this.skill = skill;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }
}
