package github.team42.ggj25;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import github.team42.ggj25.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private final boolean debug;
    private GameScreen gameScreen;

    public Main(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void create() {
        gameScreen = new GameScreen(debug);
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
        super.dispose();
    }
}
