package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Camera;
import github.team42.ggj25.GameElement;
import github.team42.ggj25.SoundManager;
import github.team42.ggj25.gamestate.GameState;
import github.team42.ggj25.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public abstract class Weapon implements GameElement {
    private float fireRate;
    private final GameState gameState;
    protected final Frog frog;
    private float coolDown = 0;
    private final List<Skill> appliedWeaponSkills = new ArrayList<>();
    private float projectileHeight = 5.0f;
    private float projectileWidth = 5.0f;
    private final SoundManager sounds;

    protected Weapon(GameState gameState, Frog frog, float fireRate, float initialProjectWidth, float initialProjectHeight) {
        this.gameState = gameState;
        this.sounds = gameState.getSounds();
        System.out.print("Sounds Weapon: " );
        System.out.println(gameState.getSounds());
        this.frog = frog;
        this.fireRate = fireRate;
        this.projectileHeight = initialProjectHeight;
        this.projectileWidth = initialProjectWidth;
    }

    @Override
    public void update(float deltaInSeconds) {
        coolDown += deltaInSeconds;
        if (coolDown > fireRate) {
            coolDown = 0;
            this.gameState.addProjectile(createProjectile(gameState.getProjectileSkills(),sounds));
        }
    }

    @Override
    public void handleLeafTransition() {
        Skill skill = gameState.getSkillInLastTransition();

        // if the skill manipulates the weapon, add it to the list of applied skills
        if (skill.manipulateWeapon(this)) {
            this.appliedWeaponSkills.add(skill);
        }
    }

    protected abstract Projectile createProjectile(List<Skill> skills, SoundManager sounds);

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getProjectileWidth() {
        return projectileWidth;
    }

    public void setProjectileWidth(float projectileWidth) {
        this.projectileWidth = projectileWidth;
    }

    public float getProjectileHeight() {
        return projectileHeight;
    }

    public void setProjectileHeight(float projectileHeight) {
        this.projectileHeight = projectileHeight;
    }

    public Camera getCamera() {
        return gameState.getCamera();
    }
}
