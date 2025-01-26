package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class AnimatedEntity extends AbstractEntity {
    private final Animation<TextureRegion> animation;
    private float stateTime = 0f;


    public AnimatedEntity(TextureAtlas atlas, float frameDuration, Rectangle boundingBox) {
        super(boundingBox);
        animation = new Animation<>(frameDuration, atlas.getRegions(), Animation.PlayMode.NORMAL);
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        stateTime += deltaInSeconds;
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.animation.getKeyFrame(stateTime), getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
    }

    protected void skipToFrame(int frameNumber) {
        this.stateTime = frameNumber * animation.getFrameDuration();
    }
}
