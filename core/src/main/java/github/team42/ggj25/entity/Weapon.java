package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.GameElement;
import github.team42.ggj25.gamestate.GameState;
import github.team42.ggj25.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public abstract class Weapon implements GameElement {
    private final float fireRate;
    private final GameState gameState;
    protected final Frog frog;
    private float coolDown = 0;

    protected Weapon(GameState gameState, Frog frog, float fireRate) {
        this.gameState = gameState;
        this.frog = frog;
        this.fireRate = fireRate;
    }

    @Override
    public void update(float delta) {
        coolDown += delta;
        if (coolDown > fireRate) {
            coolDown = 0;
            this.gameState.addProjectile(createProjectile(gameState.getProjectileSkills()));
        }
    }

    protected abstract Projectile createProjectile(List<Skill> skills);

    public static class BubbleGun extends Weapon {
        public BubbleGun(GameState gameState, Frog frog) {
            super(gameState, frog, 1f);
        }

        @Override
        protected Projectile createProjectile(List<Skill> skills) {
            Gdx.app.log("foo", "bar");
            return new Projectile(
                "libgdx.png",
                new Rectangle(frog.getX(), frog.getY(), 5, 5), new Vector2(1, 0),
                100, 100, 300, skills);
        }
    }
}
