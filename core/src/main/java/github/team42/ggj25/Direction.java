package github.team42.ggj25;

import com.badlogic.gdx.Input;

public enum Direction {
    Up(Input.Keys.UP),
    Down(Input.Keys.DOWN),
    Left(Input.Keys.LEFT),
    Right(Input.Keys.RIGHT);

    public final int key;

    Direction(int key){
        this.key = key;
    }
}
