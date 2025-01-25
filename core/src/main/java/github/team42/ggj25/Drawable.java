package github.team42.ggj25;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Something that can be drawn onto the screen.
 */
public interface Drawable extends GameElement {
    void drawSprites(SpriteBatch spriteBatch);

    default void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive){}
}
