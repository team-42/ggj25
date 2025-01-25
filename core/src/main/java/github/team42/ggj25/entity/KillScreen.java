package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

public class KillScreen implements Drawable {

    private final Texture killscreenTexture;

    public KillScreen() {
        FileHandle killScreenFile = Gdx.files.internal("screne_lose.png");
        killscreenTexture = new Texture(killScreenFile);
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(killscreenTexture, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }
}
