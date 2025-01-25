package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Polygon;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.entity.*;
import github.team42.ggj25.skills.Skill;
import github.team42.ggj25.skills.SkillTrees;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GameState implements Drawable {
    private final Frog player = new Frog(this);
    private final List<Enemy> enemies = new ArrayList<>();
    private final Background background = new Background();
    private final Leaf leaf = new Leaf();
    private final Pike pike = new Pike();
    private final ScoreBoard scoreBoard = new ScoreBoard();
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    boolean lost = false;

    private final int bonusPoints = 3;
    private final float bonusPointsInterval = 1;
    private float bonusPointCooldown = 1;

    private final Map<SkillTrees, Integer> levelPerSkilltree = new EnumMap<>(SkillTrees.class);
    private final List<Skill> frogSkills = new ArrayList<>();
    private final List<Skill> projectileSkills = new ArrayList<>();


    public GameState () {
        for (SkillTrees val : SkillTrees.values()) {
            levelPerSkilltree.put(val, 0);
        }
    }

    public boolean frogInsideLeaf(float x, float y){
        List<GridPoint2> outline = background.getEdgeOfLeaf();
        List<Float> vertices = new ArrayList<Float>();

        for (GridPoint2 p : outline){
            //shapes.setColor(Color.WHITE);
            //shapes.circle(p.x, p.y, 10); // Draw a pixel at (x, y)
            vertices.add((float)p.x);
            vertices.add((float)p.y);
        }

        float[] verts = new float[vertices.size()];
        for (int i = 0 ; i < vertices.size(); i++){
            verts[i] = vertices.get(i);
        }
        Polygon polygon = new Polygon(verts);
        return polygon.contains(x, y);
    }

    @Override
    public void update(float delta) {
        if (!frogInsideLeaf(player.getX(), player.getY())){
            lost = true;
        }
        if (!lost) {
            background.update(delta);
            pike.update(delta);
            leaf.update(delta);
            for (final Enemy enemy : this.enemies) {
                enemy.update(delta);
            }
            player.update(delta);
            for (Projectile p : activeProjectiles) {
                p.update(delta);
            }
            activeProjectiles.removeIf(p -> !p.isActive());

            bonusPointCooldown -= delta;
            if (bonusPointCooldown <= 0) {
                bonusPointCooldown = bonusPointsInterval;
                scoreBoard.addPointsToScore(bonusPoints);
            }
            scoreBoard.update(delta);

            if (player.overlapsWith(pike) && !pike.getIsPreparingToAttack()) {
                lost = true;
            }
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        background.draw(spriteBatch);
        pike.draw(spriteBatch);
        leaf.draw(spriteBatch);
        for (final Enemy enemy : this.enemies) {
            enemy.draw(spriteBatch);
        }
        player.draw(spriteBatch);
        for (Projectile p : activeProjectiles) {
            p.draw(spriteBatch);
        }
        scoreBoard.draw(spriteBatch);
    }




    public void renderShapes(ShapeRenderer shapes){
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
               

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        shapes.polygon(verts); // Draw the polygon outline
        shapes.end();

    }


    public void addProjectile(Projectile toAdd) {
        this.activeProjectiles.add(toAdd);
    }

    public void addFrogSkill(Skill skill) {
        this.frogSkills.add(skill);
    }

    public void addProjectileSkill(Skill skill) {
        this.projectileSkills.add(skill);
    }

    public List<Skill> getProjectileSkills() {
        return projectileSkills;
    }
}
