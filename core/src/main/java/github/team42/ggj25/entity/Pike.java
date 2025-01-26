package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.gamestate.GameState;

/**
 * Big bad guy.
 */
public class Pike extends TexturedEntity {
    private static final int PIKE_WIDTH = 460;
    private static final int PIKE_HEIGHT = 440;
    private final Texture loomingTexture;
    private final Leaf leaf;
    private boolean isPreparingToAttack = true;
    float attackCooldown = 2;
    float cooldown = 0;
    float speed = 200;
    float posX = (float) Math.random() * Constants.WIDTH;
    float posY = (float) Math.random() * Constants.HEIGHT;
    boolean goingRight;
    boolean goingUp;
    float circle_radius = 140;


    public Pike(GameState gameState) {
        super("pike_attacking.png",
            new Rectangle(0f, 0f, PIKE_WIDTH, PIKE_HEIGHT));
        this.setPosition(posX, posY);
        loomingTexture = new Texture("pike_looming.png");
        leaf = gameState.getLeaf();
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        if (isPreparingToAttack) {
            cooldown += deltaInSeconds;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = false;
                cooldown = 0;
            }
        } else {
            cooldown += deltaInSeconds * 10;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = true;
                cooldown = 0;
                leaf.bite(new Vector2(getX(), getY()), circle_radius);
            }
        }
        if (goingRight) {
            this.setPosition(getX() + speed * deltaInSeconds, getY());
        } else {
            this.setPosition(getX() - speed * deltaInSeconds, getY());
        }
        if (goingUp) {
            this.setPosition(getX(), getY() + speed * deltaInSeconds);
        } else {
            this.setPosition(getX(), getY() - speed * deltaInSeconds);
        }
        if (getX() - speed * deltaInSeconds < 0 || getX() + speed * deltaInSeconds > Constants.WIDTH) {
            goingRight = !goingRight;
        }
        if (getY() - speed * deltaInSeconds < 0 || getY() + speed * deltaInSeconds > Constants.HEIGHT) {
            goingUp = !goingUp;
        }

        posX = getX() - circle_radius;
        posY = getY() - circle_radius;

//        this.setPosition(getX() + speed * delta * Constants.upNormalized, getY() + speed * delta * vector.y);

    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        if (isPreparingToAttack) {
            spriteBatch.setColor(1, 1, 1, cooldown / attackCooldown);
            spriteBatch.draw(loomingTexture, getBoundingBox().x, getBoundingBox().y, PIKE_WIDTH, PIKE_HEIGHT);
            spriteBatch.setColor(Color.WHITE);
        } else {
            super.drawSprites(spriteBatch);
        }
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
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.circle(getX(), getY(), circle_radius);
            shapeRenderer.end();
        }
    }

    public boolean getIsPreparingToAttack() {
        return isPreparingToAttack;
    }
}
