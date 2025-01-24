package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;

/**
 * Our frog.
  */
public class Frog extends Entity {
    public static final float BASE_SPEED = 1f;
    private float speed = BASE_SPEED;

    public Frog(){
        super("frog.png", new Rectangle(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 96, 54));
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
