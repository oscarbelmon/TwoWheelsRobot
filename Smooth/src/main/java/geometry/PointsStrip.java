package geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 9/3/16.
 */
public class PointsStrip {
    List<Point> points = new ArrayList<>();

    public PointsStrip() {
        super();
    }

    public PointsStrip(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public int size() {
        return points.size();
    }

    public Point get(int index) {
        return points.get(index);
    }

    public PointsStrip subList(int start, int end) {
        return new PointsStrip(points.subList(start, end));
    }

    public void addAll(PointsStrip pointsStrip) {
        points.addAll(pointsStrip.getPoints());
    }

    public Vector getTangentNormalizedAtStart() {
        return new Vector(points.get(1), points.get(0)).normalize();
    }

    public Vector getTangentNormalizedAtEnd(List<Point> points) {
        return new Vector(points.get(points.size()-1), points.get(points.size()-2)).normalize();
    }
}
