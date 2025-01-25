package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
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
        this.weapons.add(new BubbleGun(gameState, this));
    }

    @Override
    public void update(float deltaInSeconds) {
        final EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        for (final Direction d : Direction.values()) {
            if (Gdx.input.isKeyPressed(d.key) || Gdx.input.isKeyPressed(d.alternateKey)) {
                directions.add(d);
            }
        }
        if (EnumSet.of(Direction.Up).equals(directions)) {
            this.setPosition(getX(), getY() + speed * deltaInSeconds);
        } else if (EnumSet.of(Direction.Right).equals(directions)) {
            this.setPosition(this.getX() + speed * deltaInSeconds, getY());
        } else if (EnumSet.of(Direction.Down).equals(directions)) {
            this.setPosition(getX(), getY() - speed * deltaInSeconds);
        } else if (EnumSet.of(Direction.Left).equals(directions)) {
            this.setPosition(getX() - speed * deltaInSeconds, getY());
        } else if (EnumSet.of(Direction.Up, Direction.Right).equals(directions)) {
            this.setPosition((float) (getX() + speed * deltaInSeconds / Math.sqrt(2)), (float) (getY() + speed * deltaInSeconds / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Right).equals(directions)) {
            this.setPosition((float) (getX() + speed * deltaInSeconds / Math.sqrt(2)), (float) (getY() - speed * deltaInSeconds / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Up, Direction.Left).equals(directions)) {
            this.setPosition((float) (getX() - speed * deltaInSeconds / Math.sqrt(2)), (float) (getY() + speed * deltaInSeconds / Math.sqrt(2)));
        } else if (EnumSet.of(Direction.Down, Direction.Left).equals(directions)) {
            this.setPosition((float) (getX() - speed * deltaInSeconds / Math.sqrt(2)), (float) (getY() - speed * deltaInSeconds / Math.sqrt(2)));
        }

        for (final Weapon w : this.weapons) {
            w.update(deltaInSeconds);
        }
    }

    public boolean overlapsWith(Entity entity) {
        return this.getBoundingBox().overlaps(entity.getBoundingBox());
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void addSkillToWeapons() {
        weapons.forEach(weapon -> weapon.handleLeafTransition());
    }
}
