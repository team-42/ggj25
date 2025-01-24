package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.Drawable;

/**
 * A map object with a position and orientation.
 */
public interface Entity extends Drawable {
    default void draw (SpriteBatch spriteBatch){
    }
}
