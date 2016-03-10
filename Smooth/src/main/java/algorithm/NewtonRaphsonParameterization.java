package algorithm;

import geometry.CubicBezier;
import geometry.Point;
import geometry.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 10/3/16.
 */
public class NewtonRaphsonParameterization {
    private Map<Point, Double> parameterizedPoints = new HashMap<>();
    private List<Point> points;

    public NewtonRaphsonParameterization(ChordParameterization cp, CubicBezier cb) {
        super();
        points = cp.getPoints();
        parameterize(cp, cb);
    }

    private void parameterize(ChordParameterization cp, CubicBezier cb) {
        double t, numerator, denominator;
        Point qt;
        Vector qtp, qtp2;
        for(Point point: cp.getPoints()) {
            t = cp.getParameter(point);
            qt = cb.value(t);
            qtp = cb.firstDerivative(t);
            numerator = new Vector(qt, point).dotProduct(qtp);
            qtp2 = cb.secondDerivative(t);
            denominator = qtp.dotProduct(qtp) - new Vector(qt, point).dotProduct(qtp2);
            parameterizedPoints.put(point, t - numerator/denominator);
        }
    }

    public double getParameter(Point p) {
        return parameterizedPoints.get(p).doubleValue();
    }

    public void showParameterization() {
        for(Point point: points)
            System.out.println(parameterizedPoints.get(point));
    }
}
