package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.skills.Skill;

import java.util.List;

public class Projectile extends AnimatedEntity {
    private final Vector2 direction;
    private float speed;
    private float damage;
    private float remainingRange;
    private final float afterEffectRange;

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

    public Projectile(TextureAtlas atlas, Rectangle boundingBox, Vector2 direction, float speed, float damage, float range, float afterEffectRange, List<Skill> skills) {
        super(atlas, (range + afterEffectRange) / (speed * atlas.getRegions().size), boundingBox);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.remainingRange = range;
        this.afterEffectRange = afterEffectRange;
        skills.forEach(skill -> skill.manipulateProjectile(this));

    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        remainingRange -= speed * deltaInSeconds;
    }

    public boolean isActive() {
        return remainingRange > -afterEffectRange;
    }

    private boolean isHarmful() {
        return remainingRange > 0;
    }

    public boolean checkCollisionWithEnemy(List<Enemy> enemies) {
        if(!isActive() || !isHarmful()){
            return false;
        }
        for(final Enemy enemy : enemies){
            if(enemy.contains(this)){
                enemy.hit(this.damage);
                this.remainingRange = 0;
                return true;
            }
        }
        return false;
    }
}
