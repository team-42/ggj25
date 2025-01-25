package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.skills.Skill;

import java.util.List;

public class Projectile extends Entity {
    private final Vector2 direction;
    private float speed;
    private float damage;
    private float remainingRange;

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return damage;
    }

    public float getRemainingRange() {
        return remainingRange;
    }

    public void setRemainingRange(float remainingRange) {
        this.remainingRange = remainingRange;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Projectile(String textureFile, Rectangle boundingBox, Vector2 direction, float speed, float damage, float range, List<Skill> skills) {
        super(textureFile, boundingBox);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.remainingRange = range;

        skills.forEach(skill -> skill.manipulateProjectile(this));

    }

    @Override
    public void update(float deltaInSeconds) {
        // TODO this is still vector pfusch, fix plz
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        remainingRange -= speed * deltaInSeconds;
        super.update(deltaInSeconds);
    }

    public boolean isActive() {
        return remainingRange > 0;
    }
}
