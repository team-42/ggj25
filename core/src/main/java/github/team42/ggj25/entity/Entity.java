package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Drawable;

import java.awt.geom.Point2D;

/**
 * A map object with a position and orientation.
 */
public class Entity implements Drawable, Disposable {
    private final Point2D.Float position;
    private final Texture texture;

    Entity(final String textureFile, Point2D.Float position) {
        this.texture = new Texture(Gdx.files.internal(textureFile));
        this.position = position;
    }

    Entity() {
        this("libgdx.png", new Point2D.Float());
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
