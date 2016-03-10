package algorithm;

import geometry.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 10/3/16.
 */
public abstract class Parameterization {
    protected Map<Point, Double> parameterizedPoints = new HashMap<>();
    protected List<Point> points;

    public abstract void parameterize();

    protected Parameterization(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public double getParameter(Point p) {
        return parameterizedPoints.get(p).doubleValue();
    }

    public void showParameterization() {
        for(Point point: points)
            System.out.println(parameterizedPoints.get(point));
    }
}
