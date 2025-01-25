package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    public GameState() {
        for (SkillTrees val : SkillTrees.values()) {
            levelPerSkilltree.put(val, 0);
        }
        this.enemies.add(new Enemy(1600, 800));
    }

    @Override
    public void update(float deltaInSeconds) {
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

    public void addProjectileSkill(Skill skill) {
        this.projectileSkills.add(skill);
    }

    public List<Skill> getProjectileSkills() {
        return projectileSkills;
    }
}
