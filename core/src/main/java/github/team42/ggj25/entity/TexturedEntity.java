package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.FrogueUtil;

/**
 * A map object with a position and orientation.
 */
public class TexturedEntity extends AbstractEntity implements Disposable {
    private final Texture texture;
    private final Pixmap m_pixmap;
    private final Polygon hitbox;

    TexturedEntity(final String textureFile, Rectangle boundingBox) {
        this(new Texture(Gdx.files.internal(textureFile)), new Pixmap(Gdx.files.internal(textureFile)), boundingBox);
    }

    TexturedEntity(final Texture texture, Pixmap pixmap, Rectangle boundingBox) {
        super(boundingBox);
        this.texture = texture;
        this.m_pixmap = pixmap;
        this.hitbox = FrogueUtil.getEdgePolygon(this.m_pixmap);
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

    public Polygon getAccurateHitbox(){
        return this.hitbox;
    }


}
