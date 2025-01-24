package github.team42.ggj25.skills;

public abstract class Skilltree {
    private int numLevels;
    private int currentLevel;

    private Skill[] skills;

    private void init() {};

    public Skill getSkillByLevel(int level) {
        return level < numLevels ? skills[level] : null;
    }
}
