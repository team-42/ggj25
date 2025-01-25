package github.team42.ggj25;

import com.badlogic.gdx.Input;

public enum Direction {
    Up(Input.Keys.W, Input.Keys.UP),
    Down(Input.Keys.S, Input.Keys.DOWN),
    Left(Input.Keys.A, Input.Keys.LEFT),
    Right(Input.Keys.D, Input.Keys.RIGHT);

    public final int key;
    public final int alternateKey;

    Direction(int key, int alternateKey) {
        this.key = key;
        this.alternateKey = alternateKey;
    }
}
