package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.entity.Leaf;

public class SkillScreenHandler {
    private float elapsedTime; // Zeit, die seit dem Start vergangen ist
    private float duration; // Dauer der Animation (in Sekunden)

    public SkillScreenHandler() {
        init();
    }

    public void init() {
        elapsedTime = 0;
        duration = 2;
    }

    public void updateSkillScreen(float deltaInSeconds, GameState gs) {
        elapsedTime += deltaInSeconds;
        float progress = elapsedTime / duration;
        if (progress > 1f) {
            gs.setCurrentPhase(GamePhase.SKILLSCREEN_TO_LEAF);
            gs.setLeaf(new Leaf(gs.getCurrentLevel()));
        }
    }

    public void drawSkillScreen(SpriteBatch spriteBatch, GameState gs) {
        // TODO: add skillscreen
    }
}
