package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Drawable;

abstract class AbstractEntity implements Entity {
    private Rectangle boundingBox;

    AbstractEntity(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public float getX() {
        return boundingBox.getCenter(new Vector2()).x;
    }

    @Override
    public float getY() {
        return boundingBox.getCenter(new Vector2()).y;
    }

    @Override
    public void setPosition(float x, float y) {
        this.boundingBox.setCenter(x, y);
    }

    @Override
    public Rectangle getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public boolean contains(float x, float y) {
        return getBoundingBox().contains(x, y);
    }

    @Override
    public boolean contains(Entity other) {
        return this.contains(other.getX(), other.getY());
    }

}
