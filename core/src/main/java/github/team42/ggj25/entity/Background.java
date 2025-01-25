package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

/**
 * The game background, i.e. water and the pike.
 */
public class Background implements Drawable {

    private final Texture m_blue_background;
    private final Texture m_water;
    private final Texture m_water_dark;
    private final Texture m_water_with_white_puddle;
    private final Texture m_water_lily_ambient;
    private final Texture m_water_lily;

    public Background() {
        FileHandle blue_background = Gdx.files.internal("blue_background.png");
        FileHandle water = Gdx.files.internal("water.png");
        FileHandle water_dark = Gdx.files.internal("water_dark.png");
        FileHandle water_with_white_puddle = Gdx.files.internal("water_with_white_puddle.png");
        FileHandle water_lily_ambient = Gdx.files.internal("water_lily_ambient.png");
        FileHandle water_lily = Gdx.files.internal("water_lily.png");

        m_blue_background = new Texture(blue_background);
        m_water = new Texture(water);
        m_water_dark = new Texture(water_dark);
        m_water_with_white_puddle = new Texture(water_with_white_puddle);
        m_water_lily_ambient = new Texture(water_lily_ambient);
        m_water_lily = new Texture(water_lily);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        // store the worldWidth and worldHeight as local variables for brevity
        spriteBatch.draw(m_blue_background, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_with_white_puddle, 0, 0, Constants.WIDTH, Constants.HEIGHT);

    }

    public void drawAmbient(SpriteBatch spriteBatch) {
//        spriteBatch.draw(m_water_dark, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily_ambient, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }
}
