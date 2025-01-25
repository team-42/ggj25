package github.team42.ggj25.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import github.team42.ggj25.Constants;
import github.team42.ggj25.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * The game background, i.e. water and the pike.
 */
public class Background implements Drawable {

    private final Texture m_blue_background;
    private final Texture m_water;
    private final Texture m_water_dark;
    private final Texture m_white_puddle;
    private final Texture m_water_lily_ambient;
    private final Texture m_water_lily;

    Pixmap m_pixmap_leaf;

    public Background() {
        FileHandle blue_background = Gdx.files.internal("blue_background.png");
        FileHandle water = Gdx.files.internal("water.png");
        FileHandle water_dark = Gdx.files.internal("water_dark.png");
        FileHandle water_white_puddle = Gdx.files.internal("white_puddle.png");
        FileHandle water_lily_ambient = Gdx.files.internal("water_lily_ambient.png");
        FileHandle water_lily = Gdx.files.internal("water_lily.png");

        m_blue_background = new Texture(blue_background);
        m_water = new Texture(water);
        m_water_dark = new Texture(water_dark);
        m_white_puddle = new Texture(water_white_puddle);
        m_water_lily_ambient = new Texture(water_lily_ambient);
        m_water_lily = new Texture(water_lily);

        m_pixmap_leaf = new Pixmap(Gdx.files.internal("water_lily.png"));
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        // store the worldWidth and worldHeight as local variables for brevity

        spriteBatch.draw(m_blue_background, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        //spriteBatch.draw(m_water, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        spriteBatch.draw(m_water_dark, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_white_puddle, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily_ambient, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        spriteBatch.draw(m_water_lily, 0, 0, Constants.WIDTH, Constants.HEIGHT);

    }

    public List<GridPoint2> getEdgeOfLeaf(){
        int width = m_pixmap_leaf.getWidth();
        int height = m_pixmap_leaf.getHeight();
        List<GridPoint2> edge_points = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = m_pixmap_leaf.getPixel(x, y);
                int alpha = (pixel & 0x000000ff);
                GridPoint2 grid_point = new GridPoint2(x, y);
                // Check if the pixel is part of the outline
                if (alpha > 0.1 && isEdge(grid_point, m_pixmap_leaf)) {
                    // Logic to detect edge pixels
                    edge_points.add( new GridPoint2 ( x, height - y));
                }
            }
        }
        return edge_points;
    };

    boolean isEdge(GridPoint2 xy, Pixmap pixmap) {
        int x = xy.x;
        int y = xy.y;

        int alpha = (pixmap.getPixel(x, y) & 0x000000ff);
        if (alpha < 0.1) return false;

        // Check neighboring pixels
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the current pixel
                int nx = x + dx;
                int ny = y + dy;

                // Ensure neighbors are within bounds
                if (nx >= 0 && nx < pixmap.getWidth() && ny >= 0 && ny < pixmap.getHeight()) {
                    int neighborAlpha = (pixmap.getPixel(nx, ny) & 0x000000ff);
                    if (neighborAlpha <= 0.1) return true; // Found an edge
                }
            }
        }
        return false;
    }



}
