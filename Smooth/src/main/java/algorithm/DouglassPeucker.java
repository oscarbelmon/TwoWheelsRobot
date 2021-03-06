package algorithm;

import geometry.Line;
import geometry.Point;
import geometry.PointsStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 4/03/16.
 */
public class DouglassPeucker {
    private PointsStrip points;

    public DouglassPeucker(PointsStrip points) {
        super();
        this.points = points;
    }

    public PointsStrip simplify(double epsilon) {
        PointsStrip result1;
        PointsStrip result2;
        PointsStrip result = new PointsStrip();
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
            result.addPoint(points.get(0));
            result.addPoint(points.get(end));
        }
        return result;
    }
}
