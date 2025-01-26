package github.team42.ggj25.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import github.team42.ggj25.skills.InterestControl.InterestRate;
import github.team42.ggj25.skills.bubblecontrol.BubbleControl;
import github.team42.ggj25.skills.bubbledensity.BubbleDensity;

public enum SkillTrees {
    NO_SKILL(InterestRate.values(), Integer.MAX_VALUE, new Texture(Gdx.files.internal("no_skill.png")), new Texture(Gdx.files.internal("no_skill.png"))),
    BUBBLE_CONTROL(BubbleControl.values(), BubbleControl.values().length, new Texture(Gdx.files.internal("bubble_control.png")), new Texture(Gdx.files.internal("bubble_control_ulti.png"))),
    BUBBLE_DENSITY(BubbleDensity.values(), BubbleDensity.values().length, new Texture(Gdx.files.internal("bubble_density.png")), new Texture(Gdx.files.internal("bubble_density_ulti.png"))),
    ;

    private final Skilltree[] skilltreeLevels;
    private final int maxSkillLevel;
    private final Texture iconTexture;
    private final Texture iconTextureUltimate;

    SkillTrees(Skilltree[] skilltreeLevels, int maxSkillLevel, Texture iconTexture, Texture iconTextureUltimate) {
        this.skilltreeLevels = skilltreeLevels;
        this.iconTexture = iconTexture;
        this.maxSkillLevel = maxSkillLevel;

        this.iconTextureUltimate = iconTextureUltimate;
    }

    public Skill getSkillByLevel(int level) {
        if (level < 0 || level >= skilltreeLevels.length) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        return skilltreeLevels[level].getSkill();
    }

    public int getMaxSkillLevel() {
        return maxSkillLevel;
    }

    public Texture getIconTexture() {
        return iconTexture;
    }

    public Texture getIconTextureUltimate() {
        return iconTextureUltimate;
    }
}
