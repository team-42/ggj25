package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.skills.Skill;

import java.util.List;

public class Projectile extends Entity {
    private final Vector2 direction;
    private final Animation<TextureRegion> animation;
    private float speed;
    private float damage;
    private float remainingRange;
    private final float afterEffectRange;
    private float stateTime = 0f;

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
        super((Texture) null, boundingBox);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.remainingRange = range;
        this.afterEffectRange = afterEffectRange;
        final float frameDuration = (range + afterEffectRange) / (speed * atlas.getRegions().size);
        animation = new Animation<>(frameDuration, atlas.getRegions(), Animation.PlayMode.NORMAL);
        skills.forEach(skill -> skill.manipulateProjectile(this));

    }

    @Override
    public void update(float deltaInSeconds) {
        stateTime += deltaInSeconds;
        // TODO this is still vector pfusch, fix plz
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        remainingRange -= speed * deltaInSeconds;
        super.update(deltaInSeconds);
    }

    public boolean isActive() {
        return remainingRange > -afterEffectRange;
    }

    public boolean isHarmful() {
        return remainingRange > 0;
    }


    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.animation.getKeyFrame(stateTime), getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
    }
}
