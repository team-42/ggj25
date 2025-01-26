package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

public class DeathScreen implements Drawable, Disposable {

    private final Texture deathScreenTexture;

    public DeathScreen() {
        FileHandle deathScreenFile = Gdx.files.internal("screne_lose.png");
        deathScreenTexture = new Texture(deathScreenFile);
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(deathScreenTexture, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void dispose() {
        deathScreenTexture.dispose();
    }
}
