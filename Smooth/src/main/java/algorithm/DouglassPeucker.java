package algorithm;

import geometry.Line;
import geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 4/03/16.
 */
public class DouglassPeucker {
    private List<Point> points;

    public DouglassPeucker(List<Point> points) {
        super();
        this.points = points;
    }

    public List<Point> simplify(double epsilon) {
        List<Point> result1;
        List<Point> result2;
        List<Point> result = new ArrayList<>();
        double dMax = 0, d;
        int index = 0, end = points.size() - 1;
        if (end < 2) {
            return points;
        }
        for (int i = 1; i < end; i++) {
            d = new Line(points.get(0), points.get(end)).perpendicularDistance(points.get(i));
            if (d > dMax) {
                index = i;
                dMax = d;
            }
        }
        if (dMax > epsilon) {
            result1 = new DouglassPeucker(points.subList(0, index+1)).simplify(epsilon);
            result2 = new DouglassPeucker(points.subList(index, end+1)).simplify(epsilon);
            result.addAll(result1.subList(0, result1.size()-1));
            result.addAll(result2);
        } else {
            result.add(points.get(0));
            result.add(points.get(end));
        }
        return result;
    }
}
