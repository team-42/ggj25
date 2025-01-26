package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.FrogueUtil;

import java.util.Random;

/**
 * An enemy.
 */
public class Enemy extends AbstractEntity {
    private final Leaf leaf;

    private enum Orientation {
        TurningClockwise(-1),
        TurningCounterClockwise(1),
        StraightAhead(0);

        final int turnRateFactor;

        Orientation(int turnRateFactor) {
            this.turnRateFactor = turnRateFactor;
        }
    }

    public enum Mode {
        Moving("tortoise_swimming.png", false),
        Eating("tortoise_eating.png", true),
        Floating("tortoise_floating.png", true);

        final TextureRegion textureRegion;
        final boolean isForeground;

        Mode(String fileName, boolean isForeground) {
            this.textureRegion = new TextureRegion(new Texture(Gdx.files.internal(fileName)));
            this.isForeground = isForeground;
        }

        public boolean isForeground() {
            return isForeground;
        }
    }

    private static final Random R = new Random();
    private static final int IMAGE_WIDTH = 1920;
    private static final int IMAGE_HEIGHT = 1080;
    private static final float IMAGE_SCALE = 0.1f;
    private static final Vector2 IMAGE_DIRECTION = new Vector2(0, -1);
    private static final float BASE_SPEED = 50;
    private static final float TURN_RATE_DEGREES_PER_SECOND = 20;
    private static final float ORIENTATION_CHANGE_COOLDOWN_SECONDS = 1f;
    private static final float BITE_DURATION_SECONDS = 3f;
    private static final int BITE_RADIUS = 50;
    private static final int START_HP = 100;
    private final Texture enclosingBubble = new Texture(Gdx.files.internal("bubble/enclosing_bubble.png"));
    private float speed = BASE_SPEED;
    private final Vector2 direction;
    private Orientation orientation = Orientation.StraightAhead;
    private float timeSinceLastOrientationChangeSeconds = 0;
    private float chewTime = 0;
    private Mode mode = Mode.Moving;
    private int hp = START_HP;
    private boolean scored = false;

    public Enemy(Leaf toAttack, final float x, final float y, Vector2 initialDirection) {
        super(FrogueUtil.getBoundingBoxForCenter(x, y, IMAGE_WIDTH * IMAGE_SCALE, IMAGE_HEIGHT * IMAGE_SCALE));
        this.leaf = toAttack;
        if (!initialDirection.isUnit(Constants.UNIT_VECTOR_MARGIN)) {
            throw new IllegalArgumentException("Initial direction must be a unit vector");
        }
        direction = initialDirection;
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        if (!direction.isUnit(Constants.UNIT_VECTOR_MARGIN)) {
            throw new IllegalStateException("Direction is not a unit vector");
        }
        final Vector2 head = getHead();
        switch (mode) {
            case Floating -> {
                floatUp(deltaInSeconds);
            }
            case Eating -> {
                if (this.isDead()) {
                    mode = Mode.Floating;
                } else if (!leaf.contains(head.x, head.y)) {
                    mode = Mode.Moving;
                } else {
                    eat(deltaInSeconds);
                }
            }
            case Moving -> {
                if (this.isDead()) {
                    mode = Mode.Floating;
                } else if (leaf.contains(head.x, head.y)) {
                    mode = Mode.Eating;
                    chewTime = 0;
                } else {
                    move(deltaInSeconds);
                }
            }
        }
    }

    public void hit(float damage) {
        this.hp -= damage;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    private void floatUp(float deltaInSeconds) {
        this.setPosition(getX(), getY() + speed * deltaInSeconds);
    }

    private void eat(float deltaInSeconds) {
        chewTime += deltaInSeconds;
        if (chewTime > BITE_DURATION_SECONDS) {
            leaf.bite(getHead(), BITE_RADIUS);
            chewTime = 0;
        }
    }

    private void move(float deltaInSeconds) {
        this.setPosition(getX() + speed * deltaInSeconds * direction.x, getY() + speed * deltaInSeconds * direction.y);
        if (Constants.WORLD.contains(getBoundingBox())) {
            timeSinceLastOrientationChangeSeconds += deltaInSeconds;
            if (timeSinceLastOrientationChangeSeconds > ORIENTATION_CHANGE_COOLDOWN_SECONDS && R.nextFloat() < 0.02f) {
                timeSinceLastOrientationChangeSeconds = 0;
                orientation = Orientation.values()[R.nextInt(3)];
            }
            this.direction.rotateDeg(TURN_RATE_DEGREES_PER_SECOND * deltaInSeconds * orientation.turnRateFactor);
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
        spriteBatch.draw(mode.textureRegion,
            getBoundingBox().x, getBoundingBox().y, getBoundingBox().width / 2f, getBoundingBox().height / 2f, getBoundingBox().width, getBoundingBox().height,
            1, 1, direction.angleDeg(IMAGE_DIRECTION));
        if (mode.equals(Mode.Floating)) {
            Rectangle boundingBoxForBubble = FrogueUtil.getBoundingBoxForCenter(getX(), getY(), 144, 144);
            spriteBatch.draw(enclosingBubble, boundingBoxForBubble.x, boundingBoxForBubble.y, boundingBoxForBubble.width, boundingBoxForBubble.height);
        }
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive) {
        if (debugRenderingActive) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(getBoundingBox().x, getBoundingBox().y, getBoundingBox().width / 2f, getBoundingBox().height / 2f, getBoundingBox().width, getBoundingBox().height,
                1, 1, direction.angleDeg(IMAGE_DIRECTION));
            shapeRenderer.line(getX(), getY(), getX() + direction.x * 100, getY() + direction.y * 100);
            shapeRenderer.end();
            Vector2 head = getHead();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(head.x, head.y, 6);
            shapeRenderer.end();
        }
    }

    private Vector2 getHead() {
        return new Vector2(getX() + direction.x * 30, getY() + direction.y * 30);
    }

    public Mode getMode() {
        return mode;
    }

    public boolean checkScored(){
        if(isDead() && ! scored){
            scored = true;
            return true;
        }else{
            return false;
        }
    }
}
