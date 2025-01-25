package github.team42.ggj25;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FrogueUtil {

    private FrogueUtil() {
    }


    public static Rectangle getBoundingBoxForCenter(float centerX, float centerY, float width, float height) {
        Rectangle result = new Rectangle(0, 0, width, height);
        result.setCenter(centerX, centerY);
        return result;
    }

    public static List<GridPoint2> sortPointsByCentroid(List<GridPoint2> points) {
        // Calculate the centroid
        Vector2 centroid = calculateCentroid(points);

        // Sort points based on the angle relative to the centroid
        points.sort(new Comparator<GridPoint2>() {
            @Override
            public int compare(GridPoint2 p1, GridPoint2 p2) {
                float angle1 = (float) Math.atan2(p1.y - centroid.y, p1.x - centroid.x);
                float angle2 = (float) Math.atan2(p2.y - centroid.y, p2.x - centroid.x);
                return Float.compare(angle1, angle2);
            }
        });

        return points;
    }

    public static Vector2 calculateCentroid(List<GridPoint2> points) {
        float centroidX = 0, centroidY = 0;
        for (GridPoint2 point : points) {
            centroidX += point.x;
            centroidY += point.y;
        }
        centroidX /= points.size();
        centroidY /= points.size();

        return new Vector2(centroidX, centroidY);
    }

    static public List<GridPoint2> getEdgeOfPixmap(Pixmap pixmap){
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        List<GridPoint2> edge_points = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixmap.getPixel(x, y);
                int alpha = (pixel & 0x000000ff);
                GridPoint2 grid_point = new GridPoint2(x, y);
                // Check if the pixel is part of the outline
                if (alpha > 0.1 && isEdge(grid_point, pixmap)) {
                    // Logic to detect edge pixels
                    edge_points.add( new GridPoint2 ( x, height - y));
                }
            }
        }

        edge_points = sortPointsByCentroid(edge_points);
        return edge_points;
    };

    static boolean isEdge(GridPoint2 xy, Pixmap pixmap) {
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

    static public Polygon getEdgePolygon(Pixmap pixmap){
        List<GridPoint2> outline = getEdgeOfPixmap(pixmap);;

        List<Float> vertices = new ArrayList<Float>();

        for (GridPoint2 p : outline) {
            vertices.add((float) p.x);
            vertices.add((float) p.y);
        }

    float[] verts = new float[vertices.size() + 2];
        for (int i = 0; i < vertices.size(); i++) {
        verts[i] = vertices.get(i);
    }
    verts[vertices.size()] = vertices.get(0);
    verts[vertices.size() + 1] = vertices.get(1);
    Polygon polygon = new Polygon(verts);
    return polygon;

}}
