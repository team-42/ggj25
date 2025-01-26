package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.FrogueUtil;

public class SkillScreenToLeafHandler {
    private float elapsedTime; // Zeit, die seit dem Start vergangen ist
    private float duration; // Dauer der Animation (in Sekunden)
    private Vector2 startPoint; // Startpunkt der Bewegung
    private Vector2 endPoint; // Zielpunkt (rechter Rand)
    private float controlPointOffset; // Offset für die Kontrollpunkte der Kurve

    public SkillScreenToLeafHandler () {
        init();
    }

    public void init() {
        elapsedTime = 0;
        duration = 3;
        endPoint = new Vector2((float)Constants.WIDTH / 2.0f, (float)Constants.HEIGHT / 2.0f); // middle of the map
        controlPointOffset = Float.NaN;
    }

    public void updateSkillToLeaf(float deltaInSeconds, GameState gs) {
        if(Float.isNaN(controlPointOffset)) {
            controlPointOffset = 550.0f; // (gs.getPlayer().getBoundingBox().y + endPoint.y + gs.getPlayer().getY()) / 2;
        }

        elapsedTime += deltaInSeconds;
        float progress = Math.min(elapsedTime / duration, 1f); // Fortschritt clampen
        Vector2 originalSize = new Vector2(gs.getPlayer().getOriginalSize().width, gs.getPlayer().getOriginalSize().height);

        if (progress >= 1f) {
            // Animation abgeschlossen
            gs.setCurrentPhase(GamePhase.ON_LEAF);
            gs.getPlayer().setPosition(Constants.WIDTH / 2f, Constants.HEIGHT / 2f);

            // Größe zurücksetzen
            gs.getPlayer().getBoundingBox().setSize(originalSize.x, originalSize.y);
            return;
        }

        // Startpunkt initialisieren
        if (startPoint == null) {
            startPoint = new Vector2(-gs.getPlayer().getBoundingBox().width, Constants.HEIGHT / 2f); // Knapp links außerhalb des Bildschirms
        }

        // Kontrollpunkt für die Bezierkurve
        Vector2 controlPoint = new Vector2(
            (startPoint.x + endPoint.x) / 2, // Horizontaler Mittelpunkt
            startPoint.y - controlPointOffset // Sprung nach oben
        );

        // Aktuelle Position auf der Bezierkurve berechnen
        Vector2 currentPosition = FrogueUtil.calculateBezier(progress, startPoint, controlPoint, endPoint);

        // Position setzen
        gs.getPlayer().setPosition(
            currentPosition.x - gs.getPlayer().getBoundingBox().width / 2,
            currentPosition.y - gs.getPlayer().getBoundingBox().height / 2
        );

        // Größe anpassen (basierend auf der Distanz zum Endpunkt)
        float baseScale = 0.8f; // Startgröße (80 % der Originalgröße)
        float maxScale = 3.5f;  // Maximale Größe (150 % der Originalgröße)

        // Dynamische Skalierung basierend auf progress
        float scaleFactor = baseScale + (maxScale - baseScale) * (1 - Math.abs(progress - 0.5f) * 2);

        gs.getPlayer().getBoundingBox().setSize(
            originalSize.x * scaleFactor,
            originalSize.y * scaleFactor
        );
    }

    public void drawSkillToLeaf(SpriteBatch spriteBatch, GameState gs) {
        gs.getBackground().drawSprites(spriteBatch);
        gs.getLeaf().drawSprites(spriteBatch);
        gs.getBackground().drawAmbient(spriteBatch);
        gs.getScoreBoard().drawSprites(spriteBatch);
        gs.getPlayer().drawSprites(spriteBatch);
    }
}
