package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;

/**
 * Our frog.
  */
public class Frog extends Entity {

    public Frog(){
        super("frog.png", new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54));
    }
}
