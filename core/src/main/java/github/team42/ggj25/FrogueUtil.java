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
        edge_points = simplify(edge_points, 30, 50);
        edge_points = sortPointsByCentroid(edge_points);
        return edge_points;
    }

    private static List<GridPoint2> simplify(List<GridPoint2> edgePoints, float threshold, float maxDistance) {
        if (edgePoints.isEmpty()) {
            return Collections.emptyList();
        }

        Queue<GridPoint2> vertices = new ArrayDeque<>(edgePoints);
        List<GridPoint2> result = new ArrayList<>();
        while (!vertices.isEmpty()) {

            final Set<GridPoint2> simplificationSet = new HashSet<>();
            float distanceInSet = 0;
            while (!vertices.isEmpty() && distanceInSet < maxDistance) {
                GridPoint2 p1 = vertices.poll();
                simplificationSet.add(p1);
                if (vertices.isEmpty()) {
                    break;
                } else {
                    float dstToNext = p1.dst(vertices.peek());
                    if (dstToNext > threshold) {
                        break;
                    } else {
                        distanceInSet += dstToNext;
                    }
                }
            }
            float x = 0;
            float y = 0;
            for (GridPoint2 p : simplificationSet) {
                x += p.x;
                y += p.y;
            }
            final GridPoint2 simplified = new GridPoint2((int) (x / simplificationSet.size()), (int) (y / simplificationSet.size()));
            result.add(simplified);
        }
        return result;
    }


//    private static List<GridPoint2> simplify(List<GridPoint2> vertices) {
//        double max = 10;
//
//        int size = vertices.size();
//        GridPoint2 first = vertices.get(0);
//        GridPoint2 last = vertices.get(size - 1);
//
//        double maxDistance = 0;
//        int maxVertex = 0;
//        for (int i = 1; i < size - 1; i++) {
//            GridPoint2 v = vertices.get(i);
//            // get the distance from v to the line created by first/last
//            double d = Intersector.distanceLinePoint(first.x, first.y, last.x, last.y, v.x, v.y);
//            if (d > maxDistance) {
//                maxDistance = d;
//                maxVertex = i;
//            }
//        }
//
//        if (maxDistance >= max) {
//            // subdivide
//            List<GridPoint2> one = simplify(vertices.subList(0, maxVertex + 1));
//            List<GridPoint2> two = simplify(vertices.subList(maxVertex, size));
//            // rejoin the two (TODO without repeating the middle point)
//            List<GridPoint2> simplified = new ArrayList<GridPoint2>();
//            simplified.addAll(one);
//            simplified.addAll(two);
//            return simplified;
//        } else {
//            // return only the first/last vertices
//            List<GridPoint2> simplified = new ArrayList<GridPoint2>();
//            simplified.add(first);
//            simplified.add(last);
//            return simplified;
//        }
//    }

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
        return new Polygon(verts);
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
