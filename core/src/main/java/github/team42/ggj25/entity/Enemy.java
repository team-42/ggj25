package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.FrogueUtil;

/**
 * An enemy.
 */
public class Enemy extends Entity {

    private static final int IMAGE_WIDTH = 1920;
    private static final int IMAGE_HEIGHT = 1080;
    private static final float IMAGE_SCALE = 0.1f;
    private static final Vector2 IMAGE_DIRECTION = new Vector2(0, -1);
    private static final float BASE_SPEED = 50;
    private static final float TURN_RATE_DEGREES_PER_SECOND = 45;
    private final TextureRegion textureRegion;
    private float speed = BASE_SPEED;
    private final Vector2 direction = new Vector2(IMAGE_DIRECTION);

    public Enemy(final int x, final int y) {
        super("tortoise_swimming.png", FrogueUtil.getBoundingBoxForCenter(x, y, IMAGE_WIDTH * IMAGE_SCALE, IMAGE_HEIGHT * IMAGE_SCALE));
        textureRegion = new TextureRegion(getTexture());
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!direction.isUnit(1e-5f)) {
            throw new IllegalStateException("Direction is not a unit vector");
        }
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        this.direction.rotateDeg(TURN_RATE_DEGREES_PER_SECOND * deltaInSeconds);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion,
            getBoundingBox().x, getBoundingBox().y, getBoundingBox().width / 2f, getBoundingBox().height / 2f, getBoundingBox().width, getBoundingBox().height,
            1, 1, direction.angleDeg(IMAGE_DIRECTION));
    }

//    @Override
//    public void draw(ShapeRenderer shapeRenderer) {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.line(getX(), getY(), getX() + direction.x * 100, getY() + direction.y * 100);
//        shapeRenderer.end();
//    }
}
