package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;
import github.team42.ggj25.gamestate.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Big bad guy.
 */
public class Pike extends Entity {
    Texture circleTexture;
    private boolean isPreparingToAttack = true;
    float attackCooldown = 2;
    float cooldown = 0;
    float speed = 200;
    float posX = (float) Math.random() * Constants.WIDTH;
    float posY = (float) Math.random() * Constants.HEIGHT;
    boolean goingRight;
    boolean goingUp;
    float pike_width = 460;
    float pike_height = 440;
    float circle_radius = 100;


    public Pike(GameState gameState) {
            super("pike_attacking.png",
                new Rectangle(0f, 0f, 460, 440));
            this.setPosition(posX, posY);
            circleTexture = new Texture("pike_looming.png");;

        }

    @Override
    public void update(float deltaInSeconds) {
        if (isPreparingToAttack) {
            cooldown += deltaInSeconds;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = false;
                cooldown = 0;
            }
        } else {
            cooldown += deltaInSeconds * 10;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = true;
                cooldown = 0;
            }
        }
        if (goingRight) {
            this.setPosition(getX() + speed * deltaInSeconds, getY());
        } else {
            this.setPosition(getX() - speed * deltaInSeconds, getY());
        }
        if (goingUp) {
            this.setPosition(getX(), getY() + speed * deltaInSeconds);
        } else {
            this.setPosition(getX(), getY() - speed * deltaInSeconds);
        }
        if (getX() - speed * deltaInSeconds < 0 || getX() + speed * deltaInSeconds > Constants.WIDTH) {
            goingRight = !goingRight;
        }
        if (getY() - speed * deltaInSeconds < 0 || getY() + speed * deltaInSeconds > Constants.HEIGHT) {
            goingUp = !goingUp;
        }

        posX = getX() - circle_radius;
        posY = getY() - circle_radius;

//        this.setPosition(getX() + speed * delta * Constants.upNormalized, getY() + speed * delta * vector.y);

    }

    @Override
    public void drawSprites(SpriteBatch spriteBatch) {
        if (isPreparingToAttack) {
            spriteBatch.setColor(1, 1, 1, cooldown / attackCooldown);
            spriteBatch.draw(circleTexture, posX, posY, circle_radius * 2, circle_radius * 2);
            spriteBatch.setColor(Color.WHITE);
        } else {
            super.drawSprites(spriteBatch);
        }
    }

    public boolean getIsPreparingToAttack() {
        return isPreparingToAttack;
    }
}
