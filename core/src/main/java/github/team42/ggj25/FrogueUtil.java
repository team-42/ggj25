package github.team42.ggj25;

import com.badlogic.gdx.math.Rectangle;

public final class FrogueUtil {

    private FrogueUtil() {
    }


    public static Rectangle getBoundingBoxForCenter(float centerX, float centerY, float width, float height) {
        Rectangle result = new Rectangle(0, 0, width, height);
        result.setCenter(centerX, centerY);
        return result;
    }

}
