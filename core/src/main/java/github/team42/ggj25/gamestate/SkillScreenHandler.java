package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import github.team42.ggj25.entity.Leaf;
import github.team42.ggj25.entity.SkillScreen;
import github.team42.ggj25.skills.SkillTrees;

import java.util.*;

public class SkillScreenHandler {
    public static final int DURATION = 2;
    private float elapsedTime; // Zeit, die seit dem Start vergangen ist
    private float duration; // Dauer der Animation (in Sekunden)

    private final SkillScreen skillScreen;
    private List<SkillTrees> skillTreesToLevelUp;
    private final int numberOfSkillChoices = DURATION;
    private final EnumSet<SkillTrees> possibleSkillTreesToLevelUp;

    public SkillScreenHandler(GameState gs) {
        skillScreen = new SkillScreen();

        possibleSkillTreesToLevelUp = EnumSet.allOf(SkillTrees.class);
        skillTreesToLevelUp = new ArrayList<>();
        randomizeChoosableSkills(gs.getLevelPerSkilltree());

        skillScreen.init();

        elapsedTime = 0;
        duration = DURATION;
    }

    public void init(Map<SkillTrees, Integer> levelPerSkilltree) {
        elapsedTime = 0;
        duration = DURATION;
        skillTreesToLevelUp = new ArrayList<>();
        initNextLevelUp(levelPerSkilltree);
    }

    private void initNextLevelUp(Map<SkillTrees, Integer> levelPerSkilltree) {
        skillTreesToLevelUp.clear();
        randomizeChoosableSkills(levelPerSkilltree);
        skillScreen.init();
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

    public boolean updateSkillScreen(float deltaInSeconds, GameState gs) {
        boolean buttonpressed = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) && skillTreesToLevelUp.size() >= 1) {
            gs.addLevelToSkilltree(skillTreesToLevelUp.get(0));
            buttonpressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) && skillTreesToLevelUp.size() >= DURATION) {
            gs.addLevelToSkilltree(skillTreesToLevelUp.get(1));
            buttonpressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && skillTreesToLevelUp.size() >= 3) {
            gs.addLevelToSkilltree(skillTreesToLevelUp.get(DURATION));
            buttonpressed = true;
        }

        skillScreen.update(deltaInSeconds);
        return updatePhaseTransition(deltaInSeconds, gs) || buttonpressed;
    }

    private boolean updatePhaseTransition(float deltaInSeconds, GameState gs) {
        elapsedTime += deltaInSeconds;
        float progress = elapsedTime / duration;
        if (progress > 1f) {
            gs.setCurrentPhase(GamePhase.SKILLSCREEN_TO_LEAF);
            gs.setLeaf(new Leaf(gs.getCurrentLevel()));
        }
        return progress > 1f;
    }

    public void drawSkillScreen(SpriteBatch spriteBatch, GameState gs) {
        gs.getBackground().drawSprites(spriteBatch);
        gs.getScoreBoard().drawSprites(spriteBatch);
        skillScreen.drawSprites(spriteBatch, skillTreesToLevelUp);
    }
}
