package geometry;

import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * Created by oscar on 15/03/16.
 */
public class BezierCurve {
    private List<Vector2D> points;
    private PolynomialFunction px;
    private PolynomialFunction py;
    private int degree;
    private double length;

    public BezierCurve(List<Vector2D> points) {
        super();
        this.points = points;
        degree = points.size() - 1;
        calculatePolynomials();
        length = calculateLength();
    }

    private double calculateLength() {
        RombergIntegrator ri = new RombergIntegrator();
        return ri.integrate(1000, this::derivatives, 0, 1);
    }

    private double derivatives(double t) {
        double dpx = px.derivative().value(t);
        double dpy = py.derivative().value(t);
        return Math.sqrt(dpx*dpx + dpy*dpy);
    }

    public double getLength() {
        return length;
    }

    private void calculatePolynomials() {
        px = new PolynomialFunction(new double[]{0});
        py = new PolynomialFunction(new double[]{0});
        int counter = 0;
        PolynomialFunction berstein;
        for(Vector2D point: points) {
            berstein = new BersteinPolynomial(degree, counter).asPolynomialFunction();
            px = px.add(berstein.multiply(new PolynomialFunction(new double[]{point.getX()})));
            py = py.add(berstein.multiply(new PolynomialFunction(new double[]{point.getY()})));
            counter++;
        }
    }

    public Vector2D value(double t) {
        return new Vector2D(px.value(t), py.value(t));
    }

    private double integrateDerivatives(double min, double max) {
        RombergIntegrator ri = new RombergIntegrator();
        return ri.integrate(1000, this::derivatives, min, max);
    }

    private double integrateDerivativesFromCero(double max) {
        return integrateDerivatives(0, max);
    }

    public double inverse(double s) {
        if(s == 0) return 0;
        double threshold = 0.0000001;
        double t = s/length;
        double lower = 0, upper = 1;
        for(int i = 0; i < 10000; i++) {
            double f = integrateDerivativesFromCero(t) - s;
            if(Math.abs(f) < threshold) return t;

            double df = derivatives(t);
            double tCandidate = t - f/df;

            if(f > 0) {
                upper = t;
                if(tCandidate <= lower) t = (upper+lower)/2;
                else t = tCandidate;
            } else {
                lower = t;
                if(tCandidate >= upper) t = (upper+lower)/2;
                else t = tCandidate;
            }
        }
        return t;
    }
}
