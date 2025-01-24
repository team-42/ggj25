package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.entity.Background;
import github.team42.ggj25.entity.Enemy;
import github.team42.ggj25.entity.Frog;
import github.team42.ggj25.entity.Leaf;

import java.util.ArrayList;
import java.util.List;

public class GameState implements Drawable {
    private final Frog player = new Frog();
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private final Leaf leaf = new Leaf();


    @Override
    public void draw(SpriteBatch spriteBatch) {
        background.draw(spriteBatch);
        leaf.draw(spriteBatch);
        for (final Enemy enemy : this.enemies) {
            enemy.draw(spriteBatch);
        }
        player.draw(spriteBatch);
    }
}
