package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

/**
 * A map object with a position and orientation.
 */
public class TexturedEntity extends AbstractEntity implements Disposable {
    private final Texture texture;

    TexturedEntity(final String textureFile, Rectangle boundingBox) {
        this(new Texture(Gdx.files.internal(textureFile)), boundingBox);
    }

    TexturedEntity(final Texture texture, Rectangle boundingBox) {
        super(boundingBox);
        this.texture = texture;
    }

    TexturedEntity() {
        this("libgdx.png", new Rectangle(0f, 0f, 50f, 50f));
    }

    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    protected Texture getTexture() {
        return texture;
    }
}
