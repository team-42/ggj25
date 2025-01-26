package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;
import github.team42.ggj25.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard implements Drawable, Disposable {
    private long score = 0;

    private final Label scoreBoardLabel;
    private final Label.LabelStyle scoreBoardStyle = new Label.LabelStyle();

    private final int row_height = Constants.HEIGHT / 36;
    private final int textPadding = row_height / 2;

    // Feld für die Textur
    private Texture menueBackgroundTexture;
    private TextureRegion menueBackgroundRegion;
    private Texture frogJumpTexture;

    private float elapsedTime = 0;

    private List<Skill> skillsToApply = new ArrayList<>();
    private float progress;
    private boolean isJumpedAllowed;

    public ScoreBoard(List<Skill> skills) {
        skillsToApply = skills;
        progress = 0.0f;

        scoreBoardStyle.font = new BitmapFont(Gdx.files.internal("font.fnt"));
        scoreBoardStyle.fontColor = Color.RED;

        scoreBoardLabel = new Label("0 QUAKS", scoreBoardStyle);
        scoreBoardLabel.setSize(Gdx.graphics.getWidth(), row_height);
        scoreBoardLabel.setPosition(textPadding, Gdx.graphics.getHeight() - 2 * row_height);
        scoreBoardLabel.setAlignment(Align.center);
        scoreBoardLabel.setFontScale(2.5f);

        initializeMenueBackground();
        initializeFrogJump();
    }

    public void prepareForNextOnLeaf() {
        progress = 0.0f;
        elapsedTime = 0;
        isJumpedAllowed = false;
    }

    // Initialisierung (z. B. im Konstruktor oder `create`)
    public void initializeMenueBackground() {
        menueBackgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));

        // Definiere den unteren Bereich der Textur (untere Hälfte)
        int width = menueBackgroundTexture.getWidth();
        int fullHeight = menueBackgroundTexture.getHeight();
        int partialHeight = (int) (fullHeight * 0.37f);

        // Beginne in der Mitte der Textur (untere Hälfte)
        menueBackgroundRegion = new TextureRegion(
            menueBackgroundTexture,
            0,          // X-Koordinate (Start bei links)
            fullHeight - partialHeight, // Y-Koordinate (Start bei der Hälfte der Höhe)
            width,      // Breite der gesamten Textur
            partialHeight  // Höhe der unteren Hälfte
        );
    }

    public void initializeFrogJump() {
        // Lade die Grafik für frog_jump
        frogJumpTexture = new Texture(Gdx.files.internal("frog_jump.png"));
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        float originalWidth = menueBackgroundRegion.getRegionWidth();
        float scaledWidth = originalWidth * 1.5f; // Breite skalieren
        float x = (Constants.WIDTH - scaledWidth) / 2f; // Neu berechnete Mitte
        float y = Constants.HEIGHT - menueBackgroundRegion.getRegionHeight();

        // Zeichne die Region
        spriteBatch.draw(
            menueBackgroundRegion,
            x, y,                              // Position
            scaledWidth,
            menueBackgroundRegion.getRegionHeight()
        );

        if (progress >= 1f) {
            drawFrogJump(spriteBatch);
            isJumpedAllowed = true;
        }

        scoreBoardLabel.draw(spriteBatch, 1);
    }

    public void drawFrogJump(SpriteBatch spriteBatch) {
        // Berechnung der Position und Größe basierend auf dem Rahmenausschnitt
        float frameWidth = menueBackgroundRegion.getRegionWidth() * 2; // Rahmen ist doppelt so breit
        float frameHeight = menueBackgroundRegion.getRegionHeight();  // Höhe der unteren 38%
        float frameX = (Constants.WIDTH - frameWidth) / 2f;           // Rahmen beginnt mittig
        float frameY = Constants.HEIGHT - frameHeight;               // Rahmen am oberen Bildschirmrand

        // Berechnung des Ausschnitts für die Grafik
        float frogJumpWidth = frogJumpTexture.getWidth();
        float frogJumpHeight = frogJumpTexture.getHeight();

        // Skaliere die Grafik, um in den 38%-Rahmenausschnitt zu passen
        // TODO: use math magic for scaling
        float scaleFactor = 0.1f; //Math.min(frameHeight / frogJumpHeight, (frameWidth / 3f) / frogJumpWidth);
        float scaledWidth = frogJumpWidth * scaleFactor;
        float scaledHeight = frogJumpHeight * scaleFactor;

        // Positioniere die Grafik am rechten Rand des Rahmenausschnitts
        // TODO: use math magic for placement
        float frogJumpX = 1300;//frameX + frameWidth - scaledWidth - 10; // 10 Pixel Abstand zum Rand
        float frogJumpY = 980; //frameY + (frameHeight - scaledHeight) / 2f; // Vertikal zentrieren

        // Zeichne die Grafik
        spriteBatch.draw(frogJumpTexture, frogJumpX, frogJumpY, scaledWidth, scaledHeight);
    }

    @Override
    public void update(float deltaInSeconds) {
        Drawable.super.update(deltaInSeconds);

        // Fortschrittsberechnung basierend auf Zeit (20 Sekunden)
        progress = Math.min(elapsedTime / 20f, 1f); // Fortschritt [0, 1]
        elapsedTime += deltaInSeconds;

        // Interpolation der Farbe von Rot (1, 0, 0) zu Grün (0, 1, 0)
        float red = 1f - progress; // Start bei 1, endet bei 0
        float green = progress;   // Start bei 0, endet bei 1

        // Setze die Farbe der Schrift basierend auf dem Fortschritt
        scoreBoardStyle.fontColor = new Color(red, green, 0f, 1f);

        scoreBoardLabel.setStyle(scoreBoardStyle);

        // Aktualisiere den Text
        scoreBoardLabel.setText(score + " QUAKS");
    }

    public long addPointsToScore(long points) {
        score += points;
        return score;
    }

    public long subtractPointsFromScore(long points) {
        score -= points;
        return score;
    }

    public long getScore() {
        return score;
    }

    @Override
    public void dispose() {
        if (frogJumpTexture != null) {
            frogJumpTexture.dispose();
        }
    }

    public boolean isJumpedAllowed() {
        return isJumpedAllowed;
    }

    public void setJumpedAllowed(boolean jumpedAllowed) {
        isJumpedAllowed = jumpedAllowed;
    }
}
