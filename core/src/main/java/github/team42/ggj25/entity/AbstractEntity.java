package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Drawable;

abstract class AbstractEntity implements Drawable {
    private final Rectangle boundingBox;

    AbstractEntity(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public float getX() {
        return boundingBox.getCenter(new Vector2()).x;
    }

    public float getY() {
        return boundingBox.getCenter(new Vector2()).y;
    }

    public void setPosition(float x, float y) {
        this.boundingBox.setCenter(x, y);
    }

    protected Rectangle getBoundingBox() {
        return this.boundingBox;
    }

}
