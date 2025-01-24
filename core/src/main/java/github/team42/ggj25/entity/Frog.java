package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Direction;

import java.util.EnumSet;

/**
 * Our frog.
 */
public class Frog extends Entity {
    public static final float BASE_SPEED = 100f;
    private float speed = BASE_SPEED;

    public Frog() {
        super("frog.png", new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54));
    }

    @Override
    public void update(float delta) {
        final EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        for (final Direction d : Direction.values()) {
            if (Gdx.input.isKeyPressed(d.key)) {
                directions.add(d);
            }
        }
        if (EnumSet.of(Direction.Up).equals(directions)) {
            this.boundingBox.setY(this.boundingBox.getY() + speed * delta);
        } else if (EnumSet.of(Direction.Right).equals(directions)) {
            this.boundingBox.setX(this.boundingBox.getX() + speed * delta);
        } else if (EnumSet.of(Direction.Down).equals(directions)) {
            this.boundingBox.setY(this.boundingBox.getY() - speed * delta);
        } else if (EnumSet.of(Direction.Left).equals(directions)) {
            this.boundingBox.setX(this.boundingBox.getX() - speed * delta);
        } else if (EnumSet.of(Direction.Up, Direction.Right).equals(directions)) {
            this.boundingBox.setX((float) (this.boundingBox.getX() + speed * delta / Math.sqrt(2)));
            this.boundingBox.setY((float) (this.boundingBox.getY() + speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Right).equals(directions)) {
            this.boundingBox.setX((float) (this.boundingBox.getX() + speed * delta / Math.sqrt(2)));
            this.boundingBox.setY((float) (this.boundingBox.getY() - speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Up, Direction.Left).equals(directions)) {
            this.boundingBox.setX((float) (this.boundingBox.getX() - speed * delta / Math.sqrt(2)));
            this.boundingBox.setY((float) (this.boundingBox.getY() + speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Left).equals(directions)) {
            this.boundingBox.setX((float) (this.boundingBox.getX() - speed * delta / Math.sqrt(2)));
            this.boundingBox.setY((float) (this.boundingBox.getY() - speed * delta / Math.sqrt(2)));
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
