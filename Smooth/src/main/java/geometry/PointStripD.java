package geometry;

import algorithm.ChordParameterization;
import algorithm.NewtonRaphsonParameterizationD;
import algorithm.Parameterization;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 9/3/16.
 */
public class PointStripD {
    private List<Vector2D> points = new ArrayList<>();
    private Parameterization parameterization;

    public PointStripD() {
        super();
    }

    public PointStripD(List<Vector2D> points, Parameterization parameterization) {
        super();
        this.points = points;
        this.parameterization = parameterization;
    }

    public List<Vector2D> getPoints() {
        return points;
    }

    public void addPoint(Vector2D point) {
        points.add(point);
    }

    public int size() {
        return points.size();
    }

    public Vector2D get(int index) {
        return points.get(index);
    }

    public PointStripD subList(int start, int end) {
        return new PointStripD(points.subList(start, end), parameterization);
    }

    public void addAll(PointStripD pointStrip) {
        points.addAll(pointStrip.getPoints());
    }

    public PointStripD removeDuplicates() {
        List<Vector2D> whitoutDuplicates = new ArrayList<>();
        for (Vector2D point : points) {
            if (whitoutDuplicates.contains(point) == false)
                whitoutDuplicates.add(point);
        }
        return new PointStripD(whitoutDuplicates, parameterization);
    }

    public Vector2D getTangentNormalizedAtStart() {
        return points.get(1).subtract(points.get(0)).normalize();
    }

    public Vector2D getTangentNormalizedAtEnd() {
        return points.get(points.size() - 2).subtract(points.get(points.size() - 1)).normalize();
    }

    private Vector2D a1(double t) {
        double B1 = 3 * t * (1 - t) * (1 - t);
        return getTangentNormalizedAtStart().scalarMultiply(B1);
    }

    private Vector2D a2(double t) {
        double B2 = 3 * t * t * (1 - t);
        return getTangentNormalizedAtEnd().scalarMultiply(B2);
    }

    private double c11() {
        double c11 = 0;
        Vector2D tmp;
        for (Vector2D point : points) {
            tmp = a1(parameterization.getParameter(point));
            c11 += tmp.dotProduct(tmp);
            if (Double.isNaN(c11)) System.out.println("c11 is NaN in c11");
        }
        return c11;
    }

    private double c12() {
        double c12 = 0;
        for (Vector2D point : points) {
            c12 += a1(parameterization.getParameter(point)).dotProduct(a2(parameterization.getParameter(point)));
        }
        return c12;
    }

    private double c21() {
        return c12();
    }

    private double c22() {
        double c22 = 0;
        Vector2D tmp;
        for (Vector2D point : points) {
            tmp = a2(parameterization.getParameter(point));
            c22 += tmp.dotProduct(tmp);
        }
        return c22;
    }

    private double x1() {
        Vector2D v;
        double x1 = 0;
        double B0, B1, B2, B3;
        Vector2D p;
        double t;
        for (Vector2D point : points) {
            t = parameterization.getParameter(point);
            B0 = (1 - t) * (1 - t) * (1 - t);
            B1 = 3 * t * (1 - t) * (1 - t);
            B2 = 3 * t * t * (1 - t);
            B3 = t * t * t;
            p = points.get(0).scalarMultiply(B0)
                    .add(points.get(0).scalarMultiply(B1))
                    .add(points.get(points.size() - 1).scalarMultiply(B2))
                    .add(points.get(points.size() - 1).scalarMultiply(B3));
            v = point.subtract(p);
            x1 += v.dotProduct(a1(t));
        }
        return x1;
    }

    private double x2() {
        Vector2D v;
        double x2 = 0;
        double B0, B1, B2, B3;
        Vector2D p;
        double t;
        for (Vector2D point : points) {
            t = parameterization.getParameter(point);
            B0 = (1 - t) * (1 - t) * (1 - t);
            B1 = 3 * t * (1 - t) * (1 - t);
            B2 = 3 * t * t * (1 - t);
            B3 = t * t * t;
            p = points.get(0).scalarMultiply(B0)
                    .add(points.get(0).scalarMultiply(B1))
                    .add(points.get(points.size() - 1).scalarMultiply(B2))
                    .add(points.get(points.size() - 1).scalarMultiply(B3));
            v = point.subtract(p);
            x2 += v.dotProduct(a2(t));
        }
        return x2;
    }

    private double alpha1() {
        Vector2D h1 = new Vector2D(x1(), c12());
        Vector2D h2 = new Vector2D(x2(), c22());
        Vector2D c1 = new Vector2D(c11(), c12());
        Vector2D c2 = new Vector2D(c21(), c22());
        return determinant(h1, h2) / determinant(c1, c2);
    }

    private double alpha2() {
        Vector2D h1 = new Vector2D(c11(), x1());
        Vector2D h2 = new Vector2D(c21(), x2());
        Vector2D c1 = new Vector2D(c11(), c12());
        Vector2D c2 = new Vector2D(c21(), c22());
        return determinant(h1, h2) / determinant(c1, c2);
    }

    private double determinant(Vector2D v1, Vector2D v2) {
        double result = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        if (result == 0) {
            result = 0.01;
            System.out.println("error in determinant");
        }
        return result;
    }

    private BezierCurveD fit(Vector2D tangentAtStart, Vector2D tangentAtEnd) {
        PointStripD ps = new PointStripD();
        ps.addPoint(points.get(0));
        Vector2D p1 = points.get(0).add(tangentAtStart.scalarMultiply(alpha1()));
        ps.addPoint(p1);
        Vector2D p2 = points.get(points.size() - 1).add(tangentAtEnd.scalarMultiply(alpha2()));
        ps.addPoint(p2);
        ps.addPoint(points.get(points.size() - 1));
        return new BezierCurveD(ps);
    }

    public List<BezierCurveD> fit(double threshold, Vector2D tangetAtStart, Vector2D tangentAtEnd) {
        List<BezierCurveD> result = new ArrayList<>();
        FitError fitError = fitError(tangetAtStart, tangentAtEnd);
        if (points.size() < 4) {
            System.out.println("Corta");
            result.add(fitError.cb);
            return result;
        }
        PointStripD ps1, ps2;
        List<Vector2D> l1, l2;
        List<BezierCurveD> fe1, fe2;
        //
        for (int i = 0; i < 10; i++) {
            Parameterization parameterization = new NewtonRaphsonParameterizationD(this.parameterization, fitError.cb);
            PointStripD ps = new PointStripD(points, parameterization);
            fitError = ps.fitError(tangetAtStart, tangentAtEnd);
        }
        //
        if (fitError.totalError > threshold) {
            int indexWorst = points.indexOf(fitError.worstFittedPoint);
            if (points.size() > 6) {
                if (indexWorst <= 2) indexWorst = 3;
                else if (indexWorst + 4 >= points.size()) indexWorst = points.size() - 5;
                Vector2D tangent = points.get(indexWorst+1).subtract(points.get(indexWorst-1)).normalize();
                l1 = points.subList(0, indexWorst + 1);
                ps1 = new PointStripD(l1, new ChordParameterization(l1));
                fe1 = ps1.fit(threshold, tangetAtStart, tangent.scalarMultiply(-1));
                l2 = points.subList(indexWorst, points.size());
                ps2 = new PointStripD(l2, new ChordParameterization(l2));
                fe2 = ps2.fit(threshold, tangent, tangentAtEnd);
                result.addAll(fe1);
                result.addAll(fe2);
            } else result.add(fitError.cb);
        }  else result.add(fitError.cb);
        return result;
    }

    private FitError fitError(Vector2D tangentAtStart, Vector2D tangentAtEnd) {
        double t, d = 0, dTmp, error = 0;
        Vector2D onCurve, worstPoint = new Vector2D(0,0);
        BezierCurveD cb = fit(tangentAtStart, tangentAtEnd);
        //
        Parameterization parameterization = new NewtonRaphsonParameterizationD(this.parameterization, cb);
        PointStripD ps = new PointStripD(points, parameterization);
        cb = ps.fit(tangentAtStart, tangentAtEnd);
        //
        for (Vector2D point : points) {
            t = parameterization.getParameter(point);
            onCurve = cb.value(t);
            dTmp = onCurve.distance(point);
            error += dTmp;
            if (d < dTmp) {
                d = dTmp;
                worstPoint = point;
            }
        }
        return new FitError(cb, d, error, worstPoint);
    }

    private class FitError {
        BezierCurveD cb;
        double maxError;
        double totalError;
        Vector2D worstFittedPoint;

        public FitError(BezierCurveD cb, double maxError, double totalError, Vector2D worstFittedPoint) {
            this.cb = cb;
            this.maxError = maxError;
            this.totalError = totalError;
            this.worstFittedPoint = worstFittedPoint;
        }
    }

    @Override
    public String toString() {
        return points.toString();
    }
}
