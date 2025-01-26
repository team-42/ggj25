package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

/**
 * The game background, i.e. water and the pike.
 */
public class Background implements Drawable, Disposable {

    private final Texture m_blue_background;
    private final Texture m_water;
    private final Texture m_water_dark;
    private final Texture m_white_puddle;
    private final Texture m_water_lily_ambient;


    public Background() {
        FileHandle blue_background = Gdx.files.internal("blue_background.png");
        FileHandle water = Gdx.files.internal("water.png");
        FileHandle water_dark = Gdx.files.internal("water_dark.png");
        FileHandle water_white_puddle = Gdx.files.internal("white_puddle.png");
        FileHandle water_lily_ambient = Gdx.files.internal("water_lily_ambient.png");

        m_blue_background = new Texture(blue_background);
        m_water = new Texture(water);
        m_water_dark = new Texture(water_dark);
        m_white_puddle = new Texture(water_white_puddle);
        m_water_lily_ambient = new Texture(water_lily_ambient);
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        // store the worldWidth and worldHeight as local variables for brevity

        spriteBatch.draw(m_blue_background, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        //spriteBatch.draw(m_water, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        spriteBatch.draw(m_water_dark, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_white_puddle, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }

    public void drawAmbient(SpriteBatch spriteBatch) {
//        spriteBatch.draw(m_water_dark, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily_ambient, 0, 0, Constants.WIDTH, Constants.HEIGHT);

    }


    @Override
    public void dispose() {
        m_blue_background.dispose();
        m_water.dispose();
        m_water_dark.dispose();
        m_white_puddle.dispose();
        m_water_lily_ambient.dispose();
    }
}
