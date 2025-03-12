package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class FastTurtle extends AbstractEnemy {
    private static final Map<Mode, TextureRegion> TEXTURES = new EnumMap<>(Mode.class);

    static {
        TEXTURES.put(Mode.Moving, new TextureRegion(new Texture(Gdx.files.internal("enemy/fast_turtle_swimming.png"))));
        TEXTURES.put(Mode.Eating, new TextureRegion(new Texture(Gdx.files.internal("enemy/fast_turtle_eating.png"))));
        TEXTURES.put(Mode.Floating, new TextureRegion(new Texture(Gdx.files.internal("enemy/fast_turtle_floating.png"))));
    }

    public FastTurtle(Leaf toAttack, float x, float y, Vector2 initialDirection) {
        super(toAttack, x, y, initialDirection, 0.05f, 40, 120, 1f, 15, TEXTURES);
    }
}
