package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Drawable;

public interface Entity extends Drawable {
    float getX();

    float getY();

    void setPosition(float x, float y);

    Rectangle getBoundingBox();

    void setBoundingBox(Rectangle boundingBox);

    boolean contains(float x, float y);

    boolean contains(Entity other);
}
