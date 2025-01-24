package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Drawable;

/**
 * A map object with a position and orientation.
 */
public class Entity implements Drawable, Disposable {
    private final Rectangle boundingBox;
    private final Texture texture;

    Entity(final String textureFile, Rectangle boundingBox) {
        this.texture = new Texture(Gdx.files.internal(textureFile));
        this.boundingBox = boundingBox;
    }

    Entity() {
        this("libgdx.png", new Rectangle(0f, 0f, 50f, 50f));
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
