package github.team42.ggj25;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public final class FrogueUtil {

    public static final double EDGE_ALPHA_THRESHOLD = 0.1;

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

    public static List<GridPoint2> sortVerticesByDistance(List<GridPoint2> points) {
        if (points == null || points.isEmpty()) return points;

        List<GridPoint2> sorted = new ArrayList<>();
        List<GridPoint2> remaining = new ArrayList<>(points);

        // Start from the first point
        GridPoint2 current = remaining.remove(0);
        sorted.add(current);

        while (!remaining.isEmpty()) {
            // Find the nearest point to the current point
            GridPoint2 nearest = null;
            double nearestDistance = Double.MAX_VALUE;

            for (GridPoint2 point : remaining) {
                double distance = current.dst(point);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearest = point;
                }
            }

            // Add the nearest point to the sorted list and update the current point
            if (nearest != null) {
                sorted.add(nearest);
                remaining.remove(nearest);
                current = nearest;
            }
        }

        return sorted;
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

    static public List<GridPoint2> getEdgeOfPixmap(Pixmap pixmap) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        List<GridPoint2> edge_points = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixmap.getPixel(x, y);
                int alpha = (pixel & 0x000000ff);
                GridPoint2 grid_point = new GridPoint2(x, y);
                // Check if the pixel is part of the outline
                if (alpha > EDGE_ALPHA_THRESHOLD && isEdge(grid_point, pixmap)) {
                    // Logic to detect edge pixels
                    edge_points.add(new GridPoint2(x, height - y));
                }
            }
        }

        edge_points = sortVerticesByDistance(edge_points);
        edge_points = sortPointsByCentroid(edge_points);
        return edge_points;
    }


    static boolean isEdge(GridPoint2 xy, Pixmap pixmap) {
        int x = xy.x;
        int y = xy.y;

        int alpha = (pixmap.getPixel(x, y) & 0x000000ff);
        if (alpha < EDGE_ALPHA_THRESHOLD) return false;

        // Check neighboring pixels
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the current pixel
                int nx = x + dx;
                int ny = y + dy;

                // Ensure neighbors are within bounds

                if (nx >= 0 && nx < pixmap.getWidth() && ny >= 0 && ny < pixmap.getHeight()) {
                    int neighborAlpha = (pixmap.getPixel(nx, ny) & 0x000000ff);
                    if (neighborAlpha <= EDGE_ALPHA_THRESHOLD) return true; // Found an edge
                }
            }
        }
        return false;
    }

    static public Polygon getEdgePolygon(Pixmap pixmap) {
        List<GridPoint2> outline = getEdgeOfPixmap(pixmap);

        float[] verts = new float[outline.size() * 2 + 2];
        for (int i = 0; i < outline.size(); i++) {
            verts[i * 2] = outline.get(i).x;
            verts[i * 2 + 1] = outline.get(i).y;
        }
        // close the polygon by repeating the first point
        verts[outline.size() * 2] = outline.get(0).x;
        verts[outline.size() * 2 + 1] = outline.get(0).y;
        Polygon polygon = new Polygon(verts);
        Polygon smooth_polygon = processPolygon(polygon, 100, 1f);
        return smooth_polygon;
    }

     public static Polygon processPolygon(Polygon polygon, float windowSize, float outlierThreshold) {
            float[] vertices = polygon.getVertices();
            int numVertices = vertices.length / 2;

            // Store processed points
            List<Float> smoothedPoints = new ArrayList<>();

            // Iterate over points with a step matching the window size
            for (int i = 0; i < numVertices; i++) {
                float sumX = 0;
                float sumY = 0;
                int count = 0;

                List<Float> localDistances = new ArrayList<>();

                // Rolling window: collect nearby points within the window
                for (int j = 0; j < numVertices; j++) {
                    float dx = vertices[j * 2] - vertices[i * 2];
                    float dy = vertices[j * 2 + 1] - vertices[i * 2 + 1];
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);

                    if (distance <= windowSize) {
                        sumX += vertices[j * 2];
                        sumY += vertices[j * 2 + 1];
                        localDistances.add(distance);
                        count++;
                    }
                }

                // Remove outliers in the rolling window
                if (!localDistances.isEmpty()) {
                    float meanDistance = (float) localDistances.stream().mapToDouble(Double::valueOf).average().orElse(0);
                    float maxAllowedDistance = meanDistance * outlierThreshold;

                    sumX = 0;
                    sumY = 0;
                    count = 0;

                    for (int j = 0; j < numVertices; j++) {
                        float dx = vertices[j * 2] - vertices[i * 2];
                        float dy = vertices[j * 2 + 1] - vertices[i * 2 + 1];
                        float distance = (float) Math.sqrt(dx * dx + dy * dy);

                        if (distance <= windowSize && distance <= maxAllowedDistance) {
                            sumX += vertices[j * 2];
                            sumY += vertices[j * 2 + 1];
                            count++;
                        }
                    }
                }

                // Calculate the smoothed point
                if (count > 0) {
                    smoothedPoints.add(sumX / count);
                    smoothedPoints.add(sumY / count);
                }

                // Move to the next step in the rolling window
                i += (int) (windowSize / getPolygonAverageEdgeLength(vertices));
            }

            // Convert smoothed points back into a float array
            float[] finalVertices = new float[smoothedPoints.size()];
            for (int i = 0; i < smoothedPoints.size(); i++) {
                finalVertices[i] = smoothedPoints.get(i);
            }

            return reconstructPolygon(polygon, finalVertices);
        }

        private static float getPolygonAverageEdgeLength(float[] vertices) {
            int numVertices = vertices.length / 2;
            float totalLength = 0;

            for (int i = 0; i < numVertices; i++) {
                int next = (i + 1) % numVertices;
                float dx = vertices[next * 2] - vertices[i * 2];
                float dy = vertices[next * 2 + 1] - vertices[i * 2 + 1];
                totalLength += Math.sqrt(dx * dx + dy * dy);
            }

            return totalLength / numVertices;
        }

        private static Polygon reconstructPolygon(Polygon original, float[] vertices) {
            Polygon newPolygon = new Polygon(vertices);
            newPolygon.setPosition(original.getX(), original.getY());
            newPolygon.setOrigin(original.getOriginX(), original.getOriginY());
            newPolygon.setRotation(original.getRotation());
            newPolygon.setScale(original.getScaleX(), original.getScaleY());
            return newPolygon;
        }





    static public Vector2 calculateBezier(float t, Vector2 p0, Vector2 p1, Vector2 p2) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;

        Vector2 result = new Vector2();
        result.x = uu * p0.x + 2 * u * t * p1.x + tt * p2.x;
        result.y = uu * p0.y + 2 * u * t * p1.y + tt * p2.y;
        return result;
    }
}
