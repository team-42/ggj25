package github.team42.ggj25.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import github.team42.ggj25.Constants;
import github.team42.ggj25.gamestate.GameState;

public class GameScreen extends ScreenAdapter {
    private final SpriteBatch batch= new SpriteBatch();
    private final Texture image = new Texture("libgdx.png");
    private final GameState gameState = new GameState();
    private final Camera camera  = new PerspectiveCamera();;
    private final FitViewport viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, camera);


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        //batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
        batch.begin();
        gameState.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
