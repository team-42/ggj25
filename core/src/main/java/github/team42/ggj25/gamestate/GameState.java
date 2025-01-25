package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
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

public class GameState implements Drawable {
    private static final Random R = new Random();
    private static final float ENEMY_SPAWN_RATE_SECONDS = 3;
    private final Frog player = new Frog(this);
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private final Leaf leaf = new Leaf();
    private final Pike pike = new Pike();
    private final ScoreBoard scoreBoard = new ScoreBoard();
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

    public boolean frogInsideLeaf(float x, float y){
        return backgroundPolygon.contains(x, y);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (!frogInsideLeaf(player.getX(), player.getY())){
            lost = true;
        }
        if (!lost) {
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

            if (player.overlapsWith(pike) && !pike.getIsPreparingToAttack()) {
                lost = true;
            }

            timeSinceLastEnemySpawnSeconds += deltaInSeconds;
            if (timeSinceLastEnemySpawnSeconds > ENEMY_SPAWN_RATE_SECONDS) {
                timeSinceLastEnemySpawnSeconds = 0;
                spawnEnemy();
            }
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
    public void draw(SpriteBatch spriteBatch) {
        background.draw(spriteBatch);
        leaf.draw(spriteBatch);
        for (final Enemy enemy : this.enemies) {
            enemy.draw(spriteBatch);
        }
        background.drawAmbient(spriteBatch);
        pike.draw(spriteBatch);
        player.draw(spriteBatch);
        for (Projectile p : activeProjectiles) {
            p.draw(spriteBatch);
        }
        scoreBoard.draw(spriteBatch);
    }


    public void renderShapes(ShapeRenderer shapes){
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        shapes.polygon(verts); // Draw the polygon outline
        shapes.end();

    }

    private float[] buildLillypadPolygon() {
        List<GridPoint2> outline = background.getEdgeOfLeaf();
        List<Float> vertices = new ArrayList<Float>();

        for (GridPoint2 p : outline){
            vertices.add((float)p.x);
            vertices.add((float)p.y);
        }

        float[] verts = new float[vertices.size() + 2];
        for (int i = 0 ; i < vertices.size(); i++){
            verts[i] = vertices.get(i);
        }
        verts[vertices.size()] = vertices.get(0);
        verts[vertices.size() + 1] = vertices.get(1);
        return verts;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        background.draw(shapeRenderer);
        pike.draw(shapeRenderer);
        leaf.draw(shapeRenderer);
        for (final Enemy enemy : this.enemies) {
            enemy.draw(shapeRenderer);
        }
        player.draw(shapeRenderer);
        for (Projectile p : activeProjectiles) {
            p.draw(shapeRenderer);
        }
        scoreBoard.draw(shapeRenderer);
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
