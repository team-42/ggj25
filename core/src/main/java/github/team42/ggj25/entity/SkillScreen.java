package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.gamestate.SkillScreenHandler;
import github.team42.ggj25.skills.SkillTrees;

import java.util.List;

public class SkillScreen implements Drawable, Disposable {

    private final Texture skillScreenTexture;
    private final TextureAtlas frogJump;
    private float stateTime = 0f;

    private final int sideOffset = 300;
    private Animation<TextureAtlas.AtlasRegion> animation;

    public SkillScreen() {
        FileHandle skillScreenFile = Gdx.files.internal("menu_background.png");
        skillScreenTexture = new Texture(skillScreenFile);
        frogJump = new TextureAtlas(Gdx.files.internal("frog/animation_frogjump.txt"));
    }

    public void init() {
        animation = new Animation<>( (float) SkillScreenHandler.DURATION / frogJump.getRegions().size, frogJump.getRegions(), Animation.PlayMode.NORMAL);
        stateTime = 0f;
    }

    @Override
    public void update(float deltaInSeconds) {
        stateTime += deltaInSeconds;
    }

    public void drawSprites(SpriteBatch spriteBatch, List<SkillTrees> skillTrees) {
        spriteBatch.draw(this.animation.getKeyFrame(stateTime), 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(skillScreenTexture, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        for (int i = 0; i < skillTrees.size(); i++) {
            spriteBatch.draw(
                skillTrees.get(i).getIconTexture(),
                sideOffset + i * 420,
                (float) (Constants.HEIGHT) / 2 - 200,
                420,
                420);
        }
    }

    @Override
    public void dispose() {
        skillScreenTexture.dispose();
        frogJump.dispose();
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {

    }
}
