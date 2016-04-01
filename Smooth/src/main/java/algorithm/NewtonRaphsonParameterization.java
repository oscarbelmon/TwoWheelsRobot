package algorithm;

import geometry.BezierCurve;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Created by oscar on 31/03/16.
 */
public class NewtonRaphsonParameterization extends Parameterization {
    private Parameterization parameterization;
    private BezierCurve bc;

    public NewtonRaphsonParameterization(Parameterization parameterization, BezierCurve cb) {
        super(parameterization.getPoints());
        this.parameterization = parameterization;
        this.bc = cb;
        parameterize();
    }

    public void parameterize() {
        double t, numerator, denominator;
        Vector2D qt;
        Vector2D qtp, qtp2;
        for(Vector2D point: parameterization.getPoints()) {
            t = parameterization.getParameter(point);
            qt = bc.value(t);
            qtp = bc.firstDerivative(t);
            numerator = qt.subtract(point).dotProduct(qtp);
            qtp2 = bc.secondDerivative(t);
            denominator = qtp.dotProduct(qtp) - qt.subtract(point).dotProduct(qtp2);
            parameterizedPoints.put(point, t - numerator/denominator);
        }
    }
}
