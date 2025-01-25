package github.team42.ggj25.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import github.team42.ggj25.Constants;

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

    public Pike() {
        super("hechtmaul.jpg",
            new Rectangle(
                0,
                0,
                300,
                278));

        this.setPosition(posX, posY);
        circleTexture = new Texture("pike_looming.png");
    }

    @Override
    public void update(float delta) {
        if (isPreparingToAttack) {
            cooldown += delta;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = false;
                cooldown = 0;
            }
        } else {
            cooldown += delta * 10;
            if (cooldown >= attackCooldown) {
                isPreparingToAttack = true;
                cooldown = 0;
            }
        }
        if (goingRight) {
            this.setPosition(getX() + speed * delta, getY());
        } else {
            this.setPosition(getX() - speed * delta, getY());
        }
        if (goingUp) {
            this.setPosition(getX(), getY() + speed * delta);
        } else {
            this.setPosition(getX(), getY() - speed * delta);
        }
        if (getX() - speed * delta < 0 || getX() + speed * delta > Constants.WIDTH) {
            goingRight = !goingRight;
        }
        if (getY() - speed * delta < 0 || getY() + speed * delta > Constants.HEIGHT) {
            goingUp = !goingUp;
        }

        posX = getX() - 100;
        posY = getY() - 100;

//        this.setPosition(getX() + speed * delta * Constants.upNormalized, getY() + speed * delta * vector.y);

    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (isPreparingToAttack) {
            spriteBatch.setColor(1, 1, 1, cooldown / attackCooldown);
            spriteBatch.draw(circleTexture, posX, posY, 200, 200);
            spriteBatch.setColor(Color.WHITE);
        } else {
            super.draw(spriteBatch);
        }
    }

    public boolean getIsPreparingToAttack() {
        return isPreparingToAttack;
    }
}
