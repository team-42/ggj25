package github.team42.ggj25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public final Sound background_sound;
    public final Sound bubble_pop;
    public final Sound tortoise_bubbled;


    public SoundManager() {
        background_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/background-loop.mp3"));
        bubble_pop = Gdx.audio.newSound(Gdx.files.internal("sounds/bubble-pop.mp3"));
        tortoise_bubbled = Gdx.audio.newSound(Gdx.files.internal("sounds/tortoise_bubbled.mp3"));

    }

    public void dispose() {
        background_sound.dispose();
        bubble_pop.dispose();
        tortoise_bubbled.dispose();
    }
}
