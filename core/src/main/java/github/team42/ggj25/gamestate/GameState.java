package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.buzzer.BuzzerState;
import github.team42.ggj25.buzzer.WebSocketServerBuzzer;
import github.team42.ggj25.entity.*;
import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.SkillTrees;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static github.team42.ggj25.gamestate.GamePhase.ON_LEAF;

public class GameState implements Drawable, Disposable {
    private final Frog player = new Frog(this);
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private Leaf leaf = new Leaf();
    private Pike pike = new Pike(this);
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final DeathScreen deathScreen = new DeathScreen();
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private final Camera camera;
    boolean lost = false;

    // Buzzer Handling
    private final BuzzerState buzzerState;
    private final WebSocketServerBuzzer webSocketServerBuzzer;

    private final Map<SkillTrees, Integer> levelPerSkilltree = new EnumMap<>(SkillTrees.class);
    private Skill skillInLastTransition = null;
    private final List<Skill> frogSkills = new ArrayList<>();
    private final List<Skill> projectileSkills = new ArrayList<>();

    // Transition Handling
    private GamePhase currentPhase = ON_LEAF;
    private final OnLeafHandler onLeafHandler = new OnLeafHandler();
    private final LeafToSkillScreenHandler leafToSkillHandler = new LeafToSkillScreenHandler();
    private final SkillScreenHandler skillScreenHandler = new SkillScreenHandler();
    private final SkillScreenToLeafHandler skillScreenToLeafHandler = new SkillScreenToLeafHandler();

    public GameState(Camera camera) {
        this.buzzerState = new BuzzerState();
        this.webSocketServerBuzzer = new WebSocketServerBuzzer(13337, this.buzzerState);
        this.webSocketServerBuzzer.start();
        this.camera = camera;
        for (SkillTrees val : SkillTrees.values()) {
            levelPerSkilltree.put(val, 0);
        }
    }

    public void prepareGameStateForOnLeaf() {
        Gdx.app.log("Prepare", "Prepare for new On Leaf Phase.");
        enemies.clear();
        activeProjectiles.clear();

        // apply chosen skill
        applySkill(skillInLastTransition);
        skillInLastTransition = null;

        onLeafHandler.init();
        leafToSkillHandler.init();
        skillScreenHandler.init();
        skillScreenToLeafHandler.init();

        pike.setPosition((float) Math.random() * Constants.WIDTH, (float) Math.random() * Constants.HEIGHT);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!lost) {
            switch (currentPhase) {
                case ON_LEAF:
                    lost = onLeafHandler.updatePlayPhase(deltaInSeconds, this);
                    break;
                case LEAF_TO_SKILLSCREEN:
                    leafToSkillHandler.updateLeafToSkillScreen(deltaInSeconds, this);
                    break;
                case SKILLSCREEN:
                    skillScreenHandler.updateSkillScreen(deltaInSeconds, this);
                    break;
                case SKILLSCREEN_TO_LEAF:
                    skillScreenToLeafHandler.updateSkillToLeaf(deltaInSeconds, this);

                    if(currentPhase.equals(ON_LEAF)) prepareGameStateForOnLeaf();
                    break;
            }
        }
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        if (!lost) {
            switch (currentPhase) {
                case ON_LEAF:
                    onLeafHandler.drawCurrentGameField(spriteBatch, this);
                    break;
                case LEAF_TO_SKILLSCREEN:
                    leafToSkillHandler.drawLeafToSkill(spriteBatch, this);
                    break;
                case SKILLSCREEN:
                    skillScreenHandler.drawSkillScreen(spriteBatch, this);
                    break;
                case SKILLSCREEN_TO_LEAF:
                    skillScreenToLeafHandler.drawSkillToLeaf(spriteBatch, this);
                    break;
            }
        } else {
            deathScreen.drawSprites(spriteBatch);
            scoreBoard.drawSprites(spriteBatch);
        }
    }


    @Override
    public void drawShapes(ShapeRenderer shapeRenderer, boolean debugRenderingActive) {
        background.drawShapes(shapeRenderer, debugRenderingActive);
        pike.drawShapes(shapeRenderer, debugRenderingActive);
        leaf.drawShapes(shapeRenderer, debugRenderingActive);
        for (final Enemy enemy : this.enemies) {
            enemy.drawShapes(shapeRenderer, debugRenderingActive);
        }
        player.drawShapes(shapeRenderer, debugRenderingActive);
        for (Projectile p : activeProjectiles) {
            p.drawShapes(shapeRenderer, debugRenderingActive);
        }
        scoreBoard.drawShapes(shapeRenderer, debugRenderingActive);
//        if (debugRenderingActive) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            Rectangle box = player.getAccurateHitbox().getBoundingRectangle();
//            shapeRenderer.rect(box.x, box.y, box.width, box.height);
            //shapeRenderer.polygon(player.getAccurateHitbox().getVertices());
//            Rectangle box2 = pike.getAccurateHitbox().getBoundingRectangle();
            //shapeRenderer.polygon(pike.getAccurateHitbox().getVertices());
//            shapeRenderer.rect(box2.x, box2.y, box2.width, box2.height);
//            shapeRenderer.end();
//        }
    }

    public void addProjectile(Projectile toAdd) {
        this.activeProjectiles.add(toAdd);
    }

    public void addFrogSkill(Skill skill) {
        this.frogSkills.add(skill);
    }

    public List<Skill> getProjectileSkills() {
        return projectileSkills;
    }

    public Skill getSkillInLastTransition() {
        return skillInLastTransition;
    }

    public void finalizeTransition(Skill skill) {
        this.applySkill(skill);
    }

    private void applySkill(Skill skill) {
        if(skill == null) return;

        // add skill to all weapons if necessary
        this.player.addSkillToWeapons();

        // add skill to projectiles
        this.projectileSkills.add(skill);
    }

    public Camera getCamera() {
        return camera;
    }

    public Frog getPlayer() {
        return player;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Background getBackground() {
        return background;
    }

    public BuzzerState getBuzzerState() {
        return buzzerState;
    }

    public Pike getPike() {
        return pike;
    }

    public void setPike(Pike pike) {
        this.pike = pike;
    }

    public Leaf getLeaf() {
        return leaf;
    }

    public void setLeaf(Leaf leaf) {
        this.leaf = leaf;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    @Override
    public void dispose() {
        player.dispose();
        background.dispose();
        leaf.dispose();
        pike.dispose();
        scoreBoard.dispose();
        deathScreen.dispose();
        try {
            this.webSocketServerBuzzer.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
