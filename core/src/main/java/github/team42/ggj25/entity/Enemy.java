package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.FrogueUtil;

import java.util.Random;

/**
 * An enemy.
 */
public class Enemy extends Entity {
    private static final Random R = new Random();
    private static final int IMAGE_WIDTH = 1920;
    private static final int IMAGE_HEIGHT = 1080;
    private static final float IMAGE_SCALE = 0.1f;
    private static final Vector2 IMAGE_DIRECTION = new Vector2(0, -1);
    private static final float BASE_SPEED = 50;
    private static final float TURN_RATE_DEGREES_PER_SECOND = 20;
    private final TextureRegion textureRegion;
    private float speed = BASE_SPEED;
    private final Vector2 direction;
    private boolean rotateClockwise = true;

    public Enemy(final float x, final float y, Vector2 initialDirection) {
        super("tortoise_swimming.png", FrogueUtil.getBoundingBoxForCenter(x, y, IMAGE_WIDTH * IMAGE_SCALE, IMAGE_HEIGHT * IMAGE_SCALE));
        if (!initialDirection.isUnit(Constants.UNIT_VECTOR_MARGIN)) {
            throw new IllegalArgumentException("Initial direction must be a unit vector");
        }
        direction = initialDirection;
        textureRegion = new TextureRegion(getTexture());
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!direction.isUnit(Constants.UNIT_VECTOR_MARGIN)) {
            throw new IllegalStateException("Direction is not a unit vector");
        }
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        if (Constants.WORLD.contains(getBoundingBox())) {
            if (R.nextFloat() < 0.02f) {
                rotateClockwise = !rotateClockwise;
            }
            this.direction.rotateDeg(TURN_RATE_DEGREES_PER_SECOND * deltaInSeconds * (rotateClockwise ? -1 : 1));
        } else {
            Vector2 center = new Vector2(Constants.WIDTH / 2f, Constants.HEIGHT / 2f);
            Vector2 dirToCenter = new Vector2(getX(), getY()).sub(center);
            float angleToCenter = dirToCenter.angleDeg(direction);
            if (Math.abs(angleToCenter - 180) > 0.1) {
                this.direction.rotateDeg(TURN_RATE_DEGREES_PER_SECOND * deltaInSeconds);
            }
        }
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
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
