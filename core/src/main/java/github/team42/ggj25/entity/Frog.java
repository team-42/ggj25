package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Direction;
import github.team42.ggj25.gamestate.GameState;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Our frog.
 */
public class Frog extends TexturedEntity {
    public static final float BASE_SPEED = 100f;
    private float speed = BASE_SPEED;
    private final List<Weapon> weapons = new ArrayList<>();

    public Frog(GameState gameState) {
        super("frog.png", new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54));
        this.weapons.add(new BubbleGun(gameState, this));
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
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

    public boolean overlapsWith(TexturedEntity entity) {
        Polygon polygon_frog = this.getAccurateHitbox();
        Polygon polygon_other = entity.getAccurateHitbox();
        if (polygon_frog.getBoundingRectangle().overlaps(polygon_other.getBoundingRectangle())){
                return Intersector.overlapConvexPolygons(polygon_frog, polygon_other);
        }
        return false;
        //return this.getBoundingBox().overlaps(entity.getBoundingBox());
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive) {
        super.drawShapes(shapeRenderer, debugRenderingActive);
        if (debugRenderingActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            float[] vertices = this.getAccurateHitbox().getTransformedVertices();
            shapeRenderer.polygon(vertices);
            shapeRenderer.end();
        }
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

    public Rectangle getOriginalSize() {
        return new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54);
    }
}
