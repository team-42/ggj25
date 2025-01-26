package github.team42.ggj25.skills.InterestControl;

import github.team42.ggj25.skills.Skill;

/**
 * Tree: Interest Rate
 * every Level: Increase Interest
 * increase interest rate by 1%.
 */
public class IncreaseInterest extends Skill {
    public IncreaseInterest() {
        super(0,
            "Increase Interest",
            "This skill increases the interest range by 1%!");
    }

    public int increaseInterestBy() {
        return 1;
    }
}
