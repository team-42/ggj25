package github.team42.ggj25;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import github.team42.ggj25.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {


    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
