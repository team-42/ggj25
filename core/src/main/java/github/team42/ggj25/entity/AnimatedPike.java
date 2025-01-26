package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.skills.Skill;

import java.util.List;

public class AnimatedPike extends AnimatedEntity {

    public AnimatedPike(TextureAtlas atlas, Rectangle boundingBox){
        super(atlas, (1f) / (atlas.getRegions().size), boundingBox);

    }
}
