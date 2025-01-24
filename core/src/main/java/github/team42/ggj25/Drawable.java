package github.team42.ggj25;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Something that can be drawn onto the screen.
 */
public interface Drawable {
    default void update(float delta) {
    }

    void draw(SpriteBatch spriteBatch);
}
