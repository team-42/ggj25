package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Entity {
    private final Vector2 vector;
    private final float speed;
    private final float damage;
    private float remainingRange;

    public Projectile(String textureFile, Rectangle boundingBox, Vector2 vector, float speed, float damage, float range) {
        super(textureFile, boundingBox);
        this.vector = vector;
        this.speed = speed;
        this.damage = damage;
        this.remainingRange = range;
    }

    @Override
    public void update(float delta) {
        // TODO this is still vector pfusch, fix plz
        this.setPosition(getX() + speed * delta * vector.x, getY() + speed * delta * vector.y);
        remainingRange -= speed * delta;
        super.update(delta);
    }

    public boolean isActive() {
        return remainingRange > 0;
    }
}
