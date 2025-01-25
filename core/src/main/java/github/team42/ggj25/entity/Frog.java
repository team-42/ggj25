package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Direction;
import github.team42.ggj25.gamestate.GameState;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Our frog.
 */
public class Frog extends Entity {
    public static final float BASE_SPEED = 100f;
    private float speed = BASE_SPEED;
    private final List<Weapon> weapons = new ArrayList<>();

    public Frog(GameState gameState) {
        super("frog.png", new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54));
        this.weapons.add(new Weapon.BubbleGun(gameState, this));
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
            this.setPosition(getX(), getY() + speed * delta);
        } else if (EnumSet.of(Direction.Right).equals(directions)) {
            this.setPosition(this.getX() + speed * delta, getY());
        } else if (EnumSet.of(Direction.Down).equals(directions)) {
            this.setPosition(getX(), getY() - speed * delta);
        } else if (EnumSet.of(Direction.Left).equals(directions)) {
            this.setPosition(getX() - speed * delta, getY());
        } else if (EnumSet.of(Direction.Up, Direction.Right).equals(directions)) {
            this.setPosition((float) (getX() + speed * delta / Math.sqrt(2)), (float) (getY() + speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Right).equals(directions)) {
            this.setPosition((float) (getX() + speed * delta / Math.sqrt(2)), (float) (getY() - speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Up, Direction.Left).equals(directions)) {
            this.setPosition((float) (getX() - speed * delta / Math.sqrt(2)), (float) (getY() + speed * delta / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Left).equals(directions)) {
            this.setPosition((float) (getX() - speed * delta / Math.sqrt(2)), (float) (getY() - speed * delta / Math.sqrt(2)));
        }

        for (final Weapon w : this.weapons) {
            w.update(delta);
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
