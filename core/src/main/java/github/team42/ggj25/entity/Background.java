package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.gamestate.GameLevel;

/**
 * The game background, i.e. water and the pike.
 */
public class Background implements Drawable, Disposable {

    private final Texture m_blue_background;
    private final Texture m_water_dark;
    private final Texture m_white_puddle;
    private final Texture m_water_lily_ambient;
    private final Texture m_water_lily_shadow;

    private final FileHandle water_dark;
    private final FileHandle blue_background = Gdx.files.internal("blue_background.png");
    private final FileHandle water_white_puddle = Gdx.files.internal("white_puddle.png");
    private final FileHandle water_lily_ambient = Gdx.files.internal("water_lily_ambient.png");
    private final FileHandle water_lily_shadow = Gdx.files.internal("water_lily_shadow_only.png");

    public Background(GameLevel currentLevel) {
        if (currentLevel == GameLevel.LEVEL_ONE) {
            water_dark = Gdx.files.internal("water_dark.png");
        } else if (currentLevel == GameLevel.LEVEL_TWO) {
            water_dark = Gdx.files.internal("oil_traces_transparent.png");
        } else {
            water_dark = Gdx.files.internal("water_dark.png");
        }

        m_water_dark = new Texture(water_dark);

        m_blue_background = new Texture(blue_background);
        m_white_puddle = new Texture(water_white_puddle);
        m_water_lily_ambient = new Texture(water_lily_ambient);
        m_water_lily_shadow = new Texture(water_lily_shadow);
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        // store the worldWidth and worldHeight as local variables for brevity
        spriteBatch.draw(m_blue_background, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_dark, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_white_puddle, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }

    public void drawAmbient(SpriteBatch spriteBatch) {
        spriteBatch.draw(m_water_lily_ambient, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily_shadow, 0, 0, Constants.WIDTH, Constants.HEIGHT);

    }

    @Override
    public void dispose() {
        m_blue_background.dispose();
        m_water_dark.dispose();
        m_white_puddle.dispose();
        m_water_lily_ambient.dispose();
    }
}
