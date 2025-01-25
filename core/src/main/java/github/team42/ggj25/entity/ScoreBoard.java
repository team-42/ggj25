package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

public class ScoreBoard implements Drawable, Disposable {
    private long score = 0;

    private final Label scoreBoardLabel;
    private final Label.LabelStyle scoreBoardStyle = new Label.LabelStyle();

    private final int row_height = Constants.HEIGHT / 16;
    private final int textPadding = row_height / 2;
//    int col_width = Gdx.graphics.getWidth() / 24;

    public ScoreBoard() {
        super();

        scoreBoardStyle.font = new BitmapFont(Gdx.files.internal("font.fnt"));
        scoreBoardStyle.fontColor = Color.RED;

        scoreBoardLabel = new Label("0 QUAKS", scoreBoardStyle);
        scoreBoardLabel.setSize(Gdx.graphics.getWidth(), row_height);
        scoreBoardLabel.setPosition(textPadding, Gdx.graphics.getHeight() - 2 * row_height);
        scoreBoardLabel.setAlignment(Align.center);
        scoreBoardLabel.setFontScale(2.5f);
    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        scoreBoardLabel.draw(spriteBatch, 1);
    }

    @Override
    public void update(float deltaInSeconds) {
        Drawable.super.update(deltaInSeconds);
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
    }
}
