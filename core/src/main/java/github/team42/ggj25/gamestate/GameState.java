package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.entity.*;
import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.SkillTrees;

import java.util.*;

import static github.team42.ggj25.gamestate.GamePhase.PLAY;

public class GameState implements Drawable {
    private GamePhase currentPhase = PLAY;

    private static final Random R = new Random();
    private static final float ENEMY_SPAWN_RATE_SECONDS = 3;
    private final Frog player = new Frog(this);
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private final Leaf leaf = new Leaf();
    private final Pike pike = new Pike(this);
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final KillScreen killScreen = new KillScreen();
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private final Camera camera;
    private final float[] verts;
    private final Polygon backgroundPolygon;
    boolean lost = false;

    private final int bonusPoints = 3;
    private final float bonusPointsInterval = 1;
    private float bonusPointCooldown = 1;
    private float timeSinceLastEnemySpawnSeconds = ENEMY_SPAWN_RATE_SECONDS;

    private final Map<SkillTrees, Integer> levelPerSkilltree = new EnumMap<>(SkillTrees.class);
    private Skill skillInLastTransition = null;
    private final List<Skill> frogSkills = new ArrayList<>();
    private final List<Skill> projectileSkills = new ArrayList<>();


    public GameState(Camera camera) {
        this.camera = camera;
        for (SkillTrees val : SkillTrees.values()) {
            levelPerSkilltree.put(val, 0);
        }
        verts = buildLillypadPolygon();
        backgroundPolygon = new Polygon(verts);
    }

    public boolean frogInsideLeaf(float x, float y) {
        return backgroundPolygon.contains(x, y);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!lost) {
            switch (currentPhase) {
                case PLAY:
                    updatePlayPhase(deltaInSeconds);
                    break;
                case CUTSCENE_TO_TRANSITION:
                    updateToTransition(deltaInSeconds);
                    break;
                case TRANSITION:
                    break;
                case CUTSCENE_FROM_TRANSITION:
                    break;
            }
        } else {

        }
    }

    private float elapsedTime = 0; // Zeit, die seit dem Start vergangen ist
    private float duration = 3; // Dauer der Animation (in Sekunden)
    private Vector2 startPoint; // Startpunkt der Bewegung
    private Vector2 endPoint = new Vector2(1920, 810); // Zielpunkt (rechter Rand)
    private float controlPointOffset = (player.getBoundingBox().y + endPoint.y + player.getY()) / 2; // Offset für die Kontrollpunkte der Kurve

    private void updateToTransition(float deltaInSeconds) {
        elapsedTime += deltaInSeconds;
        float progress = elapsedTime / duration;
        if (progress > 1f) progress = 1f;

        Vector2 currentPosition;
        if (startPoint == null) {
            startPoint = new Vector2(player.getX(), player.getY());
        } else {
            currentPosition = calculateBezier(progress, startPoint,
                new Vector2((startPoint.x + endPoint.x) / 2, startPoint.y + controlPointOffset), // Kontrollpunkt
                endPoint);

            player.setPosition(currentPosition.x - player.getBoundingBox().width / 2, currentPosition.y - player.getBoundingBox().height / 2);
            player.getBoundingBox().setSize(
                (0.9f + progress) * player.getBoundingBox().width,
                (0.9f + progress) * player.getBoundingBox().height);
//                1, 1);
        }
        scoreBoard.update(deltaInSeconds);
    }

    private Vector2 calculateBezier(float t, Vector2 p0, Vector2 p1, Vector2 p2) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;

        Vector2 result = new Vector2();
        result.x = uu * p0.x + 2 * u * t * p1.x + tt * p2.x;
        result.y = uu * p0.y + 2 * u * t * p1.y + tt * p2.y;
        return result;
    }

    private void updatePlayPhase(float deltaInSeconds) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentPhase = GamePhase.CUTSCENE_TO_TRANSITION;
        }
        background.update(deltaInSeconds);
        pike.update(deltaInSeconds);
        leaf.update(deltaInSeconds);
        for (final Enemy enemy : this.enemies) {
            enemy.update(deltaInSeconds);
        }
        player.update(deltaInSeconds);
        for (Projectile p : activeProjectiles) {
            p.update(deltaInSeconds);
        }
        activeProjectiles.removeIf(p -> !p.isActive());

        bonusPointCooldown -= deltaInSeconds;
        if (bonusPointCooldown <= 0) {
            bonusPointCooldown = bonusPointsInterval;
            scoreBoard.addPointsToScore(bonusPoints);
        }
        scoreBoard.update(deltaInSeconds);

        timeSinceLastEnemySpawnSeconds += deltaInSeconds;
        if (timeSinceLastEnemySpawnSeconds > ENEMY_SPAWN_RATE_SECONDS) {
            timeSinceLastEnemySpawnSeconds = 0;
            spawnEnemy();
        }

        if (!frogInsideLeaf(player.getX(), player.getY())) {
            lost = true;
        }

        if (player.overlapsWith(pike) && !pike.getIsPreparingToAttack()) {
            lost = true;

        }
    }

    private void spawnEnemy() {
        final int spawnDirection = R.nextInt(360);
        final Vector2 spawnVectorFromCenter = new Vector2(1100, 0);
        spawnVectorFromCenter.rotateDeg(spawnDirection);
        final Vector2 initialDirection = new Vector2(spawnVectorFromCenter);
        spawnVectorFromCenter.add(new Vector2(Constants.WIDTH / 2f, Constants.HEIGHT / 2f));
        initialDirection.rotateDeg(180);
        initialDirection.setLength(1);
        this.enemies.add(new Enemy(spawnVectorFromCenter.x, spawnVectorFromCenter.y, initialDirection));
        Gdx.app.log("Spawn Enemy", "direction: " + spawnDirection + "; initialDirection: " + initialDirection);

    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        if (!lost) {
            switch (currentPhase) {
                case PLAY:
                    drawCurrentGameField(spriteBatch);
                    break;
                case CUTSCENE_TO_TRANSITION:
                    drawToTransition(spriteBatch);
                    break;
                case TRANSITION:
                    break;
                case CUTSCENE_FROM_TRANSITION:
                    break;
            }
        } else {
            killScreen.drawSprites(spriteBatch);
            scoreBoard.drawSprites(spriteBatch);
        }
    }

    private void drawCurrentGameField(SpriteBatch spriteBatch) {
        background.drawSprites(spriteBatch);
        leaf.drawSprites(spriteBatch);
        for (final Enemy enemy : this.enemies) {
            enemy.drawSprites(spriteBatch);
        }
        background.drawAmbient(spriteBatch);
        pike.drawSprites(spriteBatch);
        player.drawSprites(spriteBatch);
        for (Projectile p : activeProjectiles) {
            p.drawSprites(spriteBatch);
        }
        scoreBoard.drawSprites(spriteBatch);
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
        if (debugRenderingActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.polygon(verts); // Draw the polygon outline
            shapeRenderer.end();
        }
        scoreBoard.drawShapes(shapeRenderer, debugRenderingActive);
    }

    private void drawToTransition(SpriteBatch spriteBatch) {
        background.drawAmbient(spriteBatch);
        scoreBoard.drawSprites(spriteBatch);
        player.drawSprites(spriteBatch);
    }

    private float[] buildLillypadPolygon() {
        List<GridPoint2> outline = background.getEdgeOfLeaf();
        List<Float> vertices = new ArrayList<Float>();

        for (GridPoint2 p : outline) {
            vertices.add((float) p.x);
            vertices.add((float) p.y);
        }

        float[] verts = new float[vertices.size() + 2];
        for (int i = 0; i < vertices.size(); i++) {
            verts[i] = vertices.get(i);
        }
        verts[vertices.size()] = vertices.get(0);
        verts[vertices.size() + 1] = vertices.get(1);
        return verts;
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
        this.skillInLastTransition = skill;

        // add skill to all weapons if necessary
        this.player.addSkillToWeapons();

        // add skill to projectiles
        this.projectileSkills.add(skill);
    }

    public Camera getCamera() {
        return camera;
    }
}
