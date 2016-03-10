package algorithm;

import geometry.CubicBezier;
import geometry.Point;
import geometry.Vector;

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
        Point qt;
        Vector qtp, qtp2;
        for(Point point: parameterization.getPoints()) {
            t = parameterization.getParameter(point);
            qt = cb.value(t);
            qtp = cb.firstDerivative(t);
            numerator = new Vector(qt, point).dotProduct(qtp);
            qtp2 = cb.secondDerivative(t);
            denominator = qtp.dotProduct(qtp) - new Vector(qt, point).dotProduct(qtp2);
            parameterizedPoints.put(point, t - numerator/denominator);
        }
    }
}
