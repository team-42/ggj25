package github.team42.ggj25.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import github.team42.ggj25.Constants;
import github.team42.ggj25.SoundManager;
import github.team42.ggj25.gamestate.GameState;

public class GameScreen extends ScreenAdapter {
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer(100_000);
    private final Texture image = new Texture("libgdx.png");
    private final GameState gameState;
    private final Viewport viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT);
    private boolean debugRenderingActive;
    private final SoundManager sounds;

    public GameScreen(boolean debugRenderingActive) {
        this.sounds = new SoundManager();
        this.debugRenderingActive = debugRenderingActive;
        long id = this.sounds.background_sound.play(0.6f);
        this.sounds.background_sound.setLooping(id,true);
        batch.enableBlending();
        gameState = new GameState(viewport, this.sounds);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (Input.Keys.F12 == keycode) {
                    GameScreen.this.debugRenderingActive = !GameScreen.this.debugRenderingActive;
                    return true;
                } else {
                    return super.keyDown(keycode);
                }
            }
        });
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
        batch.begin();
        gameState.drawSprites(batch);
        batch.end();
        gameState.drawShapes(shapeRenderer, debugRenderingActive);
        gameState.update(delta);
    }

    @Override
    public void dispose() {
        gameState.dispose();
        batch.dispose();
        image.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
