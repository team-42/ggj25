package github.team42.ggj25.entity;

import github.team42.ggj25.FrogueUtil;

/**
 * An enemy.
 */
public class Enemy extends Entity {

    private static final int IMAGE_WIDTH = 1920;
    private static final int IMAGE_HEIGHT = 1080;
    private static final float IMAGE_SCALE = 0.1f;
    private static final int SPEED = 1;

    public Enemy(final int x, final int y) {
        super("tortoise_swimming.png", FrogueUtil.getBoundingBoxForCenter(x, y, IMAGE_WIDTH * IMAGE_SCALE, IMAGE_HEIGHT * IMAGE_SCALE));
    }
}
