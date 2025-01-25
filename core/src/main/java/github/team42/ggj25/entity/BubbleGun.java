package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import github.team42.ggj25.FrogueUtil;
import github.team42.ggj25.gamestate.GameState;
import github.team42.ggj25.skills.Skill;

import java.util.List;

public class BubbleGun extends Weapon {

    private final TextureAtlas textureAtlas;

    public BubbleGun(GameState gameState, Frog frog) {
        super(gameState, frog, 1f, 85, 64);
        textureAtlas = new TextureAtlas(Gdx.files.internal("bubble/bubble.txt"));
    }

    @Override
    protected Projectile createProjectile(List<Skill> skills) {
        final Vector3 fireVector = getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        System.out.println(fireVector);
        fireVector.sub(frog.getX(), frog.getY(), 0);
        fireVector.setLength(1);

        return new Projectile(
                textureAtlas,
                FrogueUtil.getBoundingBoxForCenter(frog.getX(), frog.getY(), getProjectileWidth(), getProjectileHeight()),
                new Vector2(fireVector.x, fireVector.y), 200, 100, 500, 100, skills);
    }
}
