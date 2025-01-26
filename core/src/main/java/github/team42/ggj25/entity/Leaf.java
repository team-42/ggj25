package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.FrogueUtil;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * The leaf where we move on.
 */
public class Leaf extends AbstractEntity implements Disposable {

    private final Polygon outline;
    private final Texture texture;
    private final Pixmap pixmap;
    private final Collection<Circle> bites = new ConcurrentLinkedDeque<>();
    private final FrameBuffer maskBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, true);

    public Leaf() {
        super(new Rectangle(0, 0, Constants.WIDTH, Constants.HEIGHT));
        pixmap = new Pixmap(Gdx.files.internal("water_lily_no_shadow.png"));
        texture = new Texture(pixmap);
//        texture = new Texture(Gdx.files.internal("water_lily.png"));
        outline = buildLillypadPolygon();
    }

    public void bite(Vector2 head, float biteRadius) {
        bites.add(new Circle(head.x, head.y, biteRadius));
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        spriteBatch.end();
        maskBuffer.begin();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        /* Disable RGB color writing, enable alpha writing to the frame buffer. */
        Gdx.gl.glColorMask(false, false, false, true);
        /* Change the blending function for our alpha map. */
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0,0, Constants.WIDTH, Constants.HEIGHT);
        shapeRenderer.end();

        /* This blending function makes it so we subtract instead of adding to the alpha map. */
        shapeRenderer.setColor(Color.CLEAR);
        for (Circle c : bites) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(c.x, c.y, c.radius);
            shapeRenderer.end();
        }
        /* Now that the buffer has our alpha, we simply draw the sprite with the mask applied. */
        Gdx.gl.glColorMask(true, true, true, true);
        /* Change the blending function so the rendered pixels alpha blend with our alpha map. */
        spriteBatch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

        spriteBatch.begin();
        spriteBatch.draw(texture, getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
        spriteBatch.end();
        /* Switch to the default blend function */
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        maskBuffer.end();
        spriteBatch.begin();
        Texture bufferTexture = maskBuffer.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(bufferTexture);
        textureRegion.flip(false, true);
        spriteBatch.draw(textureRegion, 0, 0, Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive) {
        super.drawShapes(shapeRenderer, debugRenderingActive);

        if (debugRenderingActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.polygon(outline.getVertices()); // Draw the polygon outline
            for (Circle c : bites) {
                shapeRenderer.circle(c.x, c.y, c.radius);
            }
            shapeRenderer.end();
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(getBoundingBox().x, getBoundingBox().y, getBoundingBox().width, getBoundingBox().height);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX(), getY(), 6);
            shapeRenderer.end();
        }
    }

    private Polygon getOutline() {
        return outline;
    }

    public boolean contains(float x, float y) {
        if (!getOutline().contains(x, y)) {
            return false;
        }
        for (Circle c : this.bites) {
            if (c.contains(x, y)) {
                return false;
            }
        }
        return true;
    }

    private Polygon buildLillypadPolygon() {
        return FrogueUtil.getEdgePolygon(this.pixmap);
    }

    @Override
    public void dispose() {
        maskBuffer.dispose();
    }
}
