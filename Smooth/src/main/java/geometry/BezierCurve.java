package geometry;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * Created by oscar on 30/03/16.
 */
public class BezierCurve {
    private List<Vector2D> points;
    private DerivativeStructure px;
    private DerivativeStructure py;
    private int degree;
    private double length;

//    public BezierCurve(List<Vector2D> points) {
    public BezierCurve(PointStrip ps) {
        super();
        this.points = ps.getPoints();
        degree = points.size() - 1;
        length = calculateLength();
    }

    private void calculatePolynomials(double val) {
        px = new DerivativeStructure(1, degree, 0);
        py = new DerivativeStructure(1, degree, 0);
        int counter = 0;
        DerivativeStructure berstein;
        for(Vector2D point: points) {
            berstein = new BersteinPolynomial(degree, counter).getPolynomial(val);
            px = px.add(berstein.multiply(point.getX()));
            py = py.add(berstein.multiply(point.getY()));
            counter++;
        }
    }

    private double calculateLength() {
        RombergIntegrator ri = new RombergIntegrator();
        return ri.integrate(10000, this::derivatives, 0, 1);
    }

    private double derivatives(double val) {
        calculatePolynomials(val);
        double dpx = px.getPartialDerivative(1);
        double dpy = py.getPartialDerivative(1);
        return Math.sqrt(dpx*dpx + dpy*dpy);
    }

    public double getLength() {
        return length;
    }

    public Vector2D firstDerivative(double val) {
        calculatePolynomials(val);
        return new Vector2D(px.getPartialDerivative(1), py.getPartialDerivative(1));
    }

    public Vector2D secondDerivative(double val) {
        calculatePolynomials(val);
        return new Vector2D(px.getPartialDerivative(2), py.getPartialDerivative(2));
    }

    public Vector2D value(double val) {
        calculatePolynomials(val);
        return new Vector2D(px.getValue(), py.getValue());
    }

    private double integrateDerivatives(double min, double max) {
        RombergIntegrator ri = new RombergIntegrator();
        return ri.integrate(10000, this::derivatives, min, max);
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

    public double curvature(double t) {
        Vector2D vp = firstDerivative(t);
        Vector2D vpp = secondDerivative(t);
        double numerator = vp.getX() * vpp.getY() - vp.getY() * vpp.getX();
        double denominator = Math.pow(vp.getNorm(), 3);
        return numerator/denominator;
    }

    public double curvatureRadius(double t) {
        return 1/curvature(t);
    }

    public Vector2D curvatureCenter(double t) {
        Vector2D vp = firstDerivative(t);
        double curvatureRadius = curvatureRadius(t);
        Vector2D vpPerpendicular = new Vector2D(vp.getY(), -vp.getX()).normalize().scalarMultiply(-curvatureRadius);
        Vector2D p = value(t);
        return p.add(vpPerpendicular);
    }

    public Vector2D getPoint(int index) {
        return points.get(index);
    }

    @Override
    public String toString() {
        return "BezierCurve{" +
                points +
                '}';
    }
}
