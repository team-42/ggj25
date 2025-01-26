package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.FrogueUtil;
import github.team42.ggj25.SoundManager;
import github.team42.ggj25.buzzer.BuzzerState;
import github.team42.ggj25.buzzer.WebSocketServerBuzzer;
import github.team42.ggj25.entity.*;
import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.SkillTrees;

import java.util.*;

public class GameState implements Drawable, Disposable {
    private final Frog player;
    private final List<Enemy> enemies = new ArrayList<>();

    private GameLevel currentLevel = GameLevel.LEVEL_ONE;
    private final Background background = new Background(currentLevel);
    private Leaf leaf;
    private Pike pike;
    private TextureAtlas pikeBiteAtlas;
    private AnimatedPike animatedPike;
    private final ScoreBoard scoreBoard;
    private final DeathScreen deathScreen = new DeathScreen();
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private final Viewport viewport;
    boolean lost = false;
    boolean is_paused = false;
    private float pauseTime = 0;
    private float maxPauseTime = 1;

    // Buzzer Handling
    private final BuzzerState buzzerState;
    private final WebSocketServerBuzzer webSocketServerBuzzer;

    private final Map<SkillTrees, Integer> levelPerSkilltree = new EnumMap<>(SkillTrees.class);
    private Skill skillInLastTransition = null;
    private final List<Skill> frogSkills = new ArrayList<>();
    private final List<Skill> projectileSkills = new ArrayList<>();

    // Transition Handling
    private GamePhase currentPhase = GamePhase.ON_LEAF;
    private final OnLeafHandler onLeafHandler = new OnLeafHandler();
    private final LeafToSkillScreenHandler leafToSkillHandler = new LeafToSkillScreenHandler();
    private final SkillScreenHandler skillScreenHandler;
    private final SkillScreenToLeafHandler skillScreenToLeafHandler = new SkillScreenToLeafHandler();
    private final SoundManager sounds;


    public GameState(Viewport viewport, SoundManager sounds) {
        this.leaf = new Leaf(viewport, currentLevel);
        this.pike = new Pike(this);
        this.sounds = sounds;
        this.player = new Frog(this);
        this.pikeBiteAtlas = new TextureAtlas(Gdx.files.internal("pike/animation_pikebite.txt"));
        this.animatedPike = new AnimatedPike(pikeBiteAtlas, pike.getBoundingBox());
        this.buzzerState = new BuzzerState();
        this.webSocketServerBuzzer = new WebSocketServerBuzzer(13337, this.buzzerState);
        this.webSocketServerBuzzer.start();
        this.viewport = viewport;
        for (SkillTrees val : SkillTrees.values()) {
            levelPerSkilltree.put(val, 0);
        }
        skillScreenHandler = new SkillScreenHandler(this);
        this.scoreBoard = new ScoreBoard(new ArrayList<>());

    }

    private GameLevel getRandomLevel() {
        int randomLevel = new Random().nextInt(2);
        return switch (randomLevel) {
            case 0 -> GameLevel.LEVEL_ONE;
            case 1 -> GameLevel.LEVEL_TWO;
            default -> GameLevel.LEVEL_ONE;
        };
    }

    public void prepareGameStateForOnLeaf() {
        Gdx.app.log("Prepare", "Prepare for new On Leaf Phase.");
        enemies.clear();
        activeProjectiles.clear();

        // apply chosen skill
        applySkill(skillInLastTransition);
        skillInLastTransition = null;

        onLeafHandler.init(currentLevel);
        leafToSkillHandler.init();
        skillScreenHandler.init(levelPerSkilltree);
        skillScreenToLeafHandler.init();

        scoreBoard.prepareForNextOnLeaf();

        pike = new Pike(this);
        animatedPike = new AnimatedPike(pikeBiteAtlas, pike.getBoundingBox());
    }

    @Override
    public void update(float deltaInSeconds) {
        if (lost) {
            pauseTime += deltaInSeconds;

            Rectangle bbox = FrogueUtil.getBoundingBoxForCenter(pike.getX(), pike.getY(), 600, 600);
            animatedPike.setBoundingBox(bbox);
            animatedPike.update(deltaInSeconds);

            if (pauseTime >= maxPauseTime) { // Check if 2 seconds have passed
                this.is_paused = false;
                pauseTime = 0; // Reset the timer
            }
            return; // Skip updates while paused
        }

        if (!lost) {
            switch (currentPhase) {
                case ON_LEAF:
                    lost = onLeafHandler.updateOnLeafPhase(deltaInSeconds, this);
                    break;
                case LEAF_TO_SKILLSCREEN:
                    leafToSkillHandler.updateLeafToSkillScreen(deltaInSeconds, this);
                    break;
                case SKILLSCREEN:
                    if (skillScreenHandler.updateSkillScreen(deltaInSeconds, this)) {
                        setLeaf(new Leaf(viewport, getRandomLevel()));
                        currentPhase = GamePhase.SKILLSCREEN_TO_LEAF;
                    }
                    break;
                case SKILLSCREEN_TO_LEAF:
                    skillScreenToLeafHandler.updateSkillToLeaf(deltaInSeconds, this);

                    if (currentPhase.equals(GamePhase.ON_LEAF)) prepareGameStateForOnLeaf();
                    break;
            }
        }
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        if (!lost && !is_paused) {
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
        } else if (lost && is_paused) {
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
        //if (debugRenderingActive) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            Rectangle box = player.getAccurateHitbox().getBoundingRectangle();
//            shapeRenderer.rect(box.x, box.y, box.width, box.height);
        //shapeRenderer.polygon(player.getAccurateHitbox().getVertices());
//            Rectangle box2 = pike.getAccurateHitbox().getBoundingRectangle();
        //shapeRenderer.polygon(pike.getAccurateHitbox().getVertices());
//            shapeRenderer.rect(box2.x, box2.y, box2.width, box2.height);
//            shapeRenderer.end();
        //}
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
        if (skill == null) return;

        // add skill to all weapons if necessary
        this.player.addSkillToWeapons();

        // add skill to projectiles
        this.projectileSkills.add(skill);
    }

    public Camera getCamera() {
        return viewport.getCamera();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Frog getPlayer() {
        return player;
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(GameLevel currentLevel) {
        this.currentLevel = currentLevel;
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

    public Map<SkillTrees, Integer> getLevelPerSkilltree() {
        return levelPerSkilltree;
    }

    public void addLevelToSkilltree(SkillTrees skilltrees) {
        skillInLastTransition = skilltrees.getSkillByLevel(levelPerSkilltree.get(skilltrees));
        levelPerSkilltree.put(skilltrees, levelPerSkilltree.get(skilltrees) + 1);
    }

    public AnimatedPike getAnimatedPike() {
        return animatedPike;
    }

    public void setAnimatedPike(AnimatedPike animatedPike) {
        this.animatedPike = animatedPike;
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
