package algorithm;

import geometry.CubicBezier;
import geometry.Point;
import geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Created by oscar on 10/3/16.
 */
public class NewtonRaphsonParameterization extends Parameterization {
    private Parameterization parameterization;
    private CubicBezier cb;

    public NewtonRaphsonParameterization(Parameterization parameterization, CubicBezier cb) {
        super(parameterization.getPoints());
        this.parameterization = parameterization;
        this.cb = cb;
        parameterize();
    }

    public void parameterize() {
        double t, numerator, denominator;
//        Point qt;
        Vector2D qt;
//        Vector qtp, qtp2;
        Vector2D qtp, qtp2;
//        for(Point point: parameterization.getPoints()) {
        for(Vector2D point: parameterization.getPoints()) {
            t = parameterization.getParameter(point);
            qt = cb.value(t);
            qtp = cb.firstDerivative(t);
//            numerator = new Vector(qt, point).dotProduct(qtp);
            numerator = qt.subtract(point).dotProduct(qtp);
            qtp2 = cb.secondDerivative(t);
//            denominator = qtp.dotProduct(qtp) - new Vector(qt, point).dotProduct(qtp2);
            denominator = qtp.dotProduct(qtp) - qt.subtract(point).dotProduct(qtp2);
            parameterizedPoints.put(point, t - numerator/denominator);
        }
    }
}
