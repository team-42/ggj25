package github.team42.ggj25.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.entity.Enemy;
import github.team42.ggj25.entity.Projectile;

import java.util.Random;

public class OnLeafHandler {
    private static final Random R = new Random();
    private static final float ENEMY_SPAWN_RATE_SECONDS = 3;

    // invulnerability phase
    private final float invulnerabilityDuration = 3; // Seconds
    private float elapsedTime = 0;

    // Score Handling
    private float bonusPointsInterval = 1f;
    private float bonusPointCooldown = 1;
    private float timeSinceLastEnemySpawnSeconds = ENEMY_SPAWN_RATE_SECONDS;

    public void init() {
        bonusPointCooldown = 1;
        timeSinceLastEnemySpawnSeconds = ENEMY_SPAWN_RATE_SECONDS;
        elapsedTime = 0;
    }

    public boolean updatePlayPhase(float deltaInSeconds, GameState gs) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || gs.getBuzzerState().triggeredSinceLastCheck()) {
            gs.setCurrentPhase(GamePhase.LEAF_TO_SKILLSCREEN);
            return false;
        }
        gs.getBackground().update(deltaInSeconds);
        gs.getPike().update(deltaInSeconds);
        gs.getLeaf().update(deltaInSeconds);
        for (final Enemy enemy : gs.getEnemies()) {
            enemy.update(deltaInSeconds);
        }
        gs.getPlayer().update(deltaInSeconds);
        for (Projectile p : gs.getActiveProjectiles()) {
            p.update(deltaInSeconds);
        }
        gs.getActiveProjectiles().removeIf(p -> !p.isActive());

        bonusPointCooldown += deltaInSeconds;
        if (bonusPointCooldown >= bonusPointsInterval) {
            gs.getScoreBoard().addPointsToScore((int) (bonusPointCooldown / bonusPointsInterval));
            bonusPointCooldown = 0;
            bonusPointsInterval *= 0.99f;
        }
        gs.getScoreBoard().update(deltaInSeconds);

        timeSinceLastEnemySpawnSeconds += deltaInSeconds;
        if (timeSinceLastEnemySpawnSeconds > ENEMY_SPAWN_RATE_SECONDS) {
            timeSinceLastEnemySpawnSeconds = 0;
            spawnEnemy(gs);
        }

        elapsedTime += deltaInSeconds;
        float progress = elapsedTime / invulnerabilityDuration;
        if (progress > 1f) {
            if (!gs.getLeaf().contains(gs.getPlayer().getX(), gs.getPlayer().getY())) {
                Gdx.app.log("Leaf", "Not on Leaf, you are Dead!");
                Gdx.app.log("Leaf", "Player X: " + gs.getPlayer().getX() + " Y: " + gs.getPlayer().getY());
                return true;
            }
            if (gs.getPlayer().overlapsWith(gs.getPike()) && !gs.getPike().getIsPreparingToAttack()) {
                Gdx.app.log("Pike", "You got Piked, you are Dead!");
                return true;
            }
        }

        return false;
    }

    private static void spawnEnemy(GameState gs) {
        final int spawnDirection = R.nextInt(360);
        final Vector2 spawnVectorFromCenter = new Vector2(1100, 0);
        spawnVectorFromCenter.rotateDeg(spawnDirection);
        final Vector2 initialDirection = new Vector2(spawnVectorFromCenter);
        spawnVectorFromCenter.add(new Vector2(Constants.WIDTH / 2f, Constants.HEIGHT / 2f));
        initialDirection.rotateDeg(180);
        initialDirection.setLength(1);
        gs.getEnemies().add(new Enemy(gs.getLeaf(), spawnVectorFromCenter.x, spawnVectorFromCenter.y, initialDirection));
        Gdx.app.log("Spawn Enemy", "direction: " + spawnDirection + "; initialDirection: " + initialDirection);

    }

    public void drawCurrentGameField(SpriteBatch spriteBatch, GameState gs) {
        gs.getBackground().drawSprites(spriteBatch);
        gs.getEnemies().stream().filter(enemy -> !enemy.getMode().isForeground()).forEach(enemy -> enemy.drawSprites(spriteBatch));
        gs.getBackground().drawAmbient(spriteBatch);
        gs.getLeaf().drawSprites(spriteBatch);
        gs.getEnemies().stream().filter(enemy -> enemy.getMode().isForeground()).forEach(enemy -> enemy.drawSprites(spriteBatch));
        gs.getPike().drawSprites(spriteBatch);
        gs.getPlayer().drawSprites(spriteBatch);
        for (Projectile p : gs.getActiveProjectiles()) {
            p.drawSprites(spriteBatch);
        }
        gs.getScoreBoard().drawSprites(spriteBatch);
    }
}
