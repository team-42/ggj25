package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.entity.Leaf;
import github.team42.ggj25.entity.SkillScreen;
import github.team42.ggj25.skills.SkillTrees;

import java.util.*;

public class SkillScreenHandler {
    private float elapsedTime; // Zeit, die seit dem Start vergangen ist
    private float duration; // Dauer der Animation (in Sekunden)

    private final SkillScreen skillScreen;
    private final List<SkillTrees> skillTreesToLevelUp;
    private final int numberOfSkillChoices = 2;
    private final EnumSet<SkillTrees> possibleSkillTreesToLevelUp;

    public SkillScreenHandler(GameState gs) {
        skillScreen = new SkillScreen();

        possibleSkillTreesToLevelUp = EnumSet.allOf(SkillTrees.class);
        skillTreesToLevelUp = new ArrayList<>();
        randomizeChoosableSkills(gs.getLevelPerSkilltree());

        skillScreen.init(gs.getLevelPerSkilltree().keySet().toArray(new SkillTrees[0]));

        elapsedTime = 0;
        duration = 2;
    }

    public void init(Map<SkillTrees, Integer> levelPerSkilltree) {
        elapsedTime = 0;
        duration = 2;
        initNextLevelUp(levelPerSkilltree);
    }

    private void initNextLevelUp(Map<SkillTrees, Integer> levelPerSkilltree) {
        skillTreesToLevelUp.clear();
        randomizeChoosableSkills(levelPerSkilltree);
        skillScreen.init(skillTreesToLevelUp.toArray(new SkillTrees[0]));
    }

    private void randomizeChoosableSkills(Map<SkillTrees, Integer> levelPerSkilltree) {
        for (int i = 1; i <= numberOfSkillChoices; i++) {
            SkillTrees randomSkillTrees = SkillTrees.values()[new Random().nextInt(possibleSkillTreesToLevelUp.size())];
            if (levelPerSkilltree.get(randomSkillTrees) >= randomSkillTrees.getMaxSkillLevel()) {
                possibleSkillTreesToLevelUp.remove(randomSkillTrees);
            } else {
                Gdx.app.log("SkillAdd", "Added Skill: " + randomSkillTrees.name());
                skillTreesToLevelUp.add(randomSkillTrees);
            }
        }
    }

    public void updateSkillScreen(float deltaInSeconds, GameState gs) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            gs.setSkillInLastTransition() = skillTreesToLevelUp.get(0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            skillTreesToLevelUp.get(1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            skillTreesToLevelUp.get(2);
        }

        skillScreen.update(deltaInSeconds);
        updatePhaseTransition(deltaInSeconds, gs);
    }

    private void updatePhaseTransition(float deltaInSeconds, GameState gs) {
        elapsedTime += deltaInSeconds;
        float progress = elapsedTime / duration;
        if (progress > 1f) {
            gs.setCurrentPhase(GamePhase.SKILLSCREEN_TO_LEAF);
            gs.setLeaf(new Leaf());
        }
    }

    public void drawSkillScreen(SpriteBatch spriteBatch, GameState gs) {
        gs.getBackground().drawSprites(spriteBatch);
        gs.getScoreBoard().drawSprites(spriteBatch);
        skillScreen.drawSprites(spriteBatch, skillTreesToLevelUp);
    }
}
