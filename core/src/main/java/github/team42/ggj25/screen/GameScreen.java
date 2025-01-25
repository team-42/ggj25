package github.team42.ggj25.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import github.team42.ggj25.Constants;
import github.team42.ggj25.gamestate.GameState;

public class GameScreen extends ScreenAdapter {
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapes = new ShapeRenderer(100_000);
    private final Texture image = new Texture("libgdx.png");
    private final GameState gameState = new GameState();
    private final Camera camera = new OrthographicCamera(Constants.WIDTH, Constants.HEIGHT);

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.position.set( new Vector3(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 0f));
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();
        batch.draw(image, 140, 210);
        gameState.update(delta);
        gameState.draw(batch);
        batch.end();

        shapes.setProjectionMatrix(camera.combined);
        gameState.renderShapes(shapes);

    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        shapes.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }
}
