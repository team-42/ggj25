package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.skills.SkillTrees;

public class SkillScreen implements Drawable, Disposable {

    private final Texture skillScreenTexture;
    private SkillTrees[] skillTrees = new SkillTrees[0];

    private final int sideOffset = 300;
    private final int skillOffset = 30;

    public SkillScreen() {
        FileHandle skillScreenFile = Gdx.files.internal("menu_background.png");
        skillScreenTexture = new Texture(skillScreenFile);
    }

    public void init(SkillTrees[] skillTrees) {
        this.skillTrees = skillTrees;
    }

    @Override
    public void update(float deltaInSeconds) {
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(skillScreenTexture, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        for (int i = 0; i < skillTrees.length; i++) {
            spriteBatch.draw(
                skillTrees[i].getIconTexture(),
                sideOffset + i * (skillOffset + 420),
                (float) (Constants.HEIGHT - skillTrees[i].getIconTexture().getHeight()) / 2,
                420,
                420);
        }
    }

    @Override
    public void dispose() {
        skillScreenTexture.dispose();
    }
}
