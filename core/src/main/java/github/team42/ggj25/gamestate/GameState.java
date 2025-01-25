package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.entity.*;

import java.util.ArrayList;
import java.util.List;

public class GameState implements Drawable {
    private final Frog player = new Frog(this);
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private final Leaf leaf = new Leaf();
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final List<Projectile> activeProjectiles = new ArrayList<>();

    private final int bonusPoints = 3;
    private final float bonusPointsInterval = 1;
    private float bonusPointCooldown = 1;

    @Override
    public void update(float delta) {
        background.update(delta);
        leaf.update(delta);
        for (final Enemy enemy : this.enemies) {
            enemy.update(delta);
        }
        player.update(delta);
        for (Projectile p : activeProjectiles) {
            p.update(delta);
        }
        activeProjectiles.removeIf(p -> !p.isActive());

        bonusPointCooldown -= delta;
        if (bonusPointCooldown <= 0) {
            bonusPointCooldown = bonusPointsInterval;
            scoreBoard.addPointsToScore(bonusPoints);
        }
        scoreBoard.update(delta);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        background.draw(spriteBatch);
        leaf.draw(spriteBatch);
        for (final Enemy enemy : this.enemies) {
            enemy.draw(spriteBatch);
        }
        player.draw(spriteBatch);
        for (Projectile p : activeProjectiles) {
            p.draw(spriteBatch);
        }
        scoreBoard.draw(spriteBatch);
    }

    public void addProjectile(Projectile toAdd) {
        this.activeProjectiles.add(toAdd);
    }
}
