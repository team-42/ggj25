package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;

/**
 * The leaf where we move on.
 */
public class Leaf extends TexturedEntity {

    public Leaf() {
        super(new Texture(Gdx.files.internal("water_lily.png")), new Pixmap(Gdx.files.internal("water_lily_no_shadow.png")),
            new Rectangle(0, 0, Constants.WIDTH, Constants.HEIGHT));
    }
}
