package github.team42.ggj25.entity;

import com.badlogic.gdx.math.Vector2;

public class StandardTurtle extends AbstractEnemy {
    public StandardTurtle(Leaf toAttack, float x, float y, Vector2 initialDirection) {
        super(toAttack, x, y, initialDirection);
    }
}
