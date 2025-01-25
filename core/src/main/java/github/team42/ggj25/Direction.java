package github.team42.ggj25;

import com.badlogic.gdx.Input;

public enum Direction {
    Up(Input.Keys.W),
    Down(Input.Keys.S),
    Left(Input.Keys.A),
    Right(Input.Keys.D);

    public final int key;

    Direction(int key) {
        this.key = key;
    }
}
