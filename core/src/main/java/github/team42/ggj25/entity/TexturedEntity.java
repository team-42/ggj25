package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.FrogueUtil;

/**
 * A map object with a position and orientation.
 */
public class TexturedEntity extends AbstractEntity implements Disposable {
    private final Texture texture;
    private final Pixmap pixmap;
    private final Polygon hitboxAccurate;
    private final Rectangle hitboxBoundingBox;

    TexturedEntity(final String textureFile, Rectangle boundingBox) {
        this(new Texture(Gdx.files.internal(textureFile)), new Pixmap(Gdx.files.internal(textureFile)), boundingBox);
    }

    TexturedEntity(final Texture texture, Pixmap pixmap, Rectangle boundingBox) {
        super(boundingBox);
        this.texture = texture;
        this.pixmap = pixmap;
        this.hitboxAccurate = FrogueUtil.getEdgePolygon(this.pixmap);
        this.hitboxAccurate.setScale(this.getBoundingBox().getWidth() / this.pixmap.getWidth(),
            this.getBoundingBox().getHeight() / this.pixmap.getHeight());
        hitboxBoundingBox = this.hitboxAccurate.getBoundingRectangle();
    }

    TexturedEntity() {
        this("libgdx.png", new Rectangle(0f, 0f, 50f, 50f));
    }

    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive) {
        if (debugRenderingActive) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX(), getY(), 6);
            shapeRenderer.end();
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Rectangle boundingRectangle = hitboxBoundingBox;
            shapeRenderer.rect(boundingRectangle.x, boundingRectangle.y, boundingRectangle.width, boundingRectangle.height);
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();

    }

    public Polygon getAccurateHitbox() {
        return this.hitboxAccurate;
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        hitboxAccurate.setPosition(getBoundingBox().x, getBoundingBox().y);
    }

    @Override
    public boolean contains(float x, float y) {
        return hitboxBoundingBox.contains(x, y);
    }
}
