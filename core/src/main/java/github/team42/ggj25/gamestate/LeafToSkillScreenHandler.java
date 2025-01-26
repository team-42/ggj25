package github.team42.ggj25.gamestate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.FrogueUtil;

public class LeafToSkillScreenHandler {
    private float elapsedTime; // Zeit, die seit dem Start vergangen ist
    private float duration; // Dauer der Animation (in Sekunden)
    private Vector2 startPoint; // Startpunkt der Bewegung
    private Vector2 endPoint; // Zielpunkt (rechter Rand)
    private float controlPointOffset; // Offset für die Kontrollpunkte der Kurve

    public LeafToSkillScreenHandler () {
        init();
    }

    public void init() {
        elapsedTime = 0;
        duration = 3;
        //endPoint = new Vector2(1920, 810);
        endPoint = new Vector2(2380, 405);
        controlPointOffset = Float.NaN;
    }

    public void updateLeafToSkillScreen(float deltaInSeconds, GameState gs) {
        if(Float.isNaN(controlPointOffset)) {
            controlPointOffset = 550.0f; // (gs.getPlayer().getBoundingBox().y + endPoint.y + gs.getPlayer().getY()) / 2;
        }

        Vector2 originalSize = new Vector2(gs.getPlayer().getOriginalSize().width, gs.getPlayer().getOriginalSize().height);
        elapsedTime += deltaInSeconds;
        float progress = Math.min(elapsedTime / duration, 1f);;
        if (progress >= 1f) {
            // Animation abgeschlossen
            gs.setCurrentPhase(GamePhase.SKILLSCREEN);
            gs.getPlayer().setPosition(Constants.WIDTH / 2f, Constants.HEIGHT / 2f);

            // Größe zurücksetzen
            gs.getPlayer().getBoundingBox().setSize(originalSize.x, originalSize.y);
            return;
        }

       Vector2 currentPosition;
        if (startPoint == null) {
            startPoint = new Vector2(gs.getPlayer().getX(), gs.getPlayer().getY());
        } else {
            currentPosition = FrogueUtil.calculateBezier(progress, startPoint,
                new Vector2((startPoint.x + endPoint.x) / 2, startPoint.y + controlPointOffset), // Kontrollpunkt
                endPoint);

            gs.getPlayer().setPosition(
                currentPosition.x - gs.getPlayer().getBoundingBox().width / 2,
                currentPosition.y - gs.getPlayer().getBoundingBox().height / 2
            );

            float scaleFactor = 1f + 3.5f * progress; // Skaliert sanft von 1.0 bis 1.5
            gs.getPlayer().getBoundingBox().setSize(
                originalSize.x * scaleFactor,
                originalSize.y * scaleFactor
            );
        }
        gs.getScoreBoard().update(deltaInSeconds);

        if(gs.getCurrentPhase().equals(GamePhase.SKILLSCREEN)) {
            gs.getPlayer().setPosition(endPoint.x, endPoint.y);

            Rectangle rectangle = gs.getPlayer().getOriginalSize();
            gs.getPlayer().setBoundingBox(rectangle);
        }
    }

    public void drawLeafToSkill(SpriteBatch spriteBatch, GameState gs) {
        gs.getBackground().drawSprites(spriteBatch);
        gs.getLeaf().drawSprites(spriteBatch);
        gs.getBackground().drawAmbient(spriteBatch);
        gs.getScoreBoard().drawSprites(spriteBatch);
        gs.getPlayer().drawSprites(spriteBatch);
    }
}
