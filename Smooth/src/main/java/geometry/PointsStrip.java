package geometry;

import algorithm.ChordParameterization;
import algorithm.Parameterization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 9/3/16.
 */
public class PointsStrip {
    private List<Point> points = new ArrayList<>();
    private Parameterization parameterization;

    public PointsStrip() {
        super();
    }

    public PointsStrip(List<Point> points, Parameterization parameterization) {
        super();
        this.points = points;
        this.parameterization = parameterization;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public int size() {
        return points.size();
    }

    public Point get(int index) {
        return points.get(index);
    }

    public PointsStrip subList(int start, int end) {
        return new PointsStrip(points.subList(start, end), parameterization);
    }

    public void addAll(PointsStrip pointsStrip) {
        points.addAll(pointsStrip.getPoints());
    }

    public Vector getTangentNormalizedAtStart() {
        return new Vector(points.get(1), points.get(0)).normalize();
//        return new Vector(1,0);
    }

    public Vector getTangentNormalizedAtEnd() {
        return new Vector(points.get(points.size()-2), points.get(points.size()-1)).normalize();
//        return new Vector(1,0);
    }

    private Vector a1(double t) {
        double B1 = 3*t*(1-t)*(1-t);
        return getTangentNormalizedAtStart().scale(B1);
    }

    private Vector a2(double t) {
        double B2 = 3*t*t*(1-t);
        return getTangentNormalizedAtEnd().scale(B2);
    }

    private double c11() {
        double c11 = 0;
        Vector tmp;
        for(Point point: points) {
            tmp = a1(parameterization.getParameter(point));
            c11 += tmp.dotProduct(tmp);
        }
        return c11;
    }

    private double c12() {
        double c12 = 0;
        for(Point point: points) {
            c12 += a1(parameterization.getParameter(point)).dotProduct(a2(parameterization.getParameter(point)));
        }
        return c12;
    }

    private double c21() {
        return c12();
    }

    private double c22() {
        double c22 = 0;
        Vector tmp;
        for(Point point: points) {
            tmp = a2(parameterization.getParameter(point));
            c22 += tmp.dotProduct(tmp);
        }
        return c22;
    }

    private double x1() {
        Vector v;
        double x1 = 0;
        double B0, B1, B2, B3;
        Point p;
        double t;
        for(Point point: points) {
            t = parameterization.getParameter(point);
            B0 = (1-t)*(1-t)*(1-t);
            B1 = 3*t*(1-t)*(1-t);
            B2 = 3*t*t*(1-t);
            B3 = t*t*t;
            p = points.get(0).scale(B0)
                    .sum(points.get(0).scale(B1))
                    .sum(points.get(points.size()-1).scale(B2))
                    .sum(points.get(points.size()-1).scale(B3));
            v = new Vector(point, p);
            x1 += v.dotProduct(a1(t));
        }
        return x1;
    }

    private double x2() {
        Vector v;
        double x2 = 0;
        double B0, B1, B2, B3;
        Point p;
        double t;
        for(Point point: points) {
            t = parameterization.getParameter(point);
            B0 = (1-t)*(1-t)*(1-t);
            B1 = 3*t*(1-t)*(1-t);
            B2 = 3*t*t*(1-t);
            B3 = t*t*t;
            p = points.get(0).scale(B0)
                    .sum(points.get(0).scale(B1))
                    .sum(points.get(points.size()-1).scale(B2))
                    .sum(points.get(points.size()-1).scale(B3));
            v = new Vector(point, p);
            x2 += v.dotProduct(a2(t));
        }
        return x2;
    }

    private double alpha1() {
        Vector h1 = new Vector(x1(), c12());
        Vector h2 = new Vector(x2(), c22());
        Vector c1 = new Vector(c11(), c12());
        Vector c2 = new Vector(c21(), c22());
        return determinant(h1, h2) / determinant(c1, c2);
    }

    private double alpha2() {
        Vector h1 = new Vector(c11(), x1());
        Vector h2 = new Vector(c21(), x2());
        Vector c1 = new Vector(c11(), c12());
        Vector c2 = new Vector(c21(), c22());
        return determinant(h1, h2) / determinant(c1, c2);
    }

    private double determinant(Vector v1, Vector v2) {
        return v1.getVx()*v2.getVy() - v1.getVy()*v2.getVx();
    }

    public CubicBezier fit() {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(points.get(0));
        Point p1 = points.get(0).sum(getTangentNormalizedAtStart().scale(alpha1()));
        ps.addPoint(p1);
        Point p2 = points.get(points.size()-1).sum(getTangentNormalizedAtEnd().scale(alpha2()));
        ps.addPoint(p2);
        ps.addPoint(points.get(points.size()-1));
        return new CubicBezier(ps);
    }

    public List<CubicBezier> fit(double threshold) {
        List<CubicBezier> result = new ArrayList<>();
        FitError fitError = fitError();
        PointsStrip ps1, ps2;
        List<Point> l1, l2;
        List<CubicBezier> fe1, fe2;
        if(fitError.totalError > threshold) {
            l1 = points.subList(0, points.indexOf(fitError.worstFittedPoint)+1);
            ps1 = new PointsStrip(l1, new ChordParameterization(l1));
            fe1 = ps1.fit(threshold);
            l2 = points.subList(points.indexOf(fitError.worstFittedPoint)+1, points.size());
            ps2 = new PointsStrip(l2, new ChordParameterization(l2));
            fe2 = ps2.fit(threshold);
            result.addAll(fe1);
            result.addAll(fe2);
        } else result.add(fitError.cb);
        return result;
    }

    public FitError fitError() {
        double t, d = 0, dTmp, error = 0;
        Point onCurve, worstPoint = new Point();
        CubicBezier cb = fit();
        for(Point point: points) {
            t = parameterization.getParameter(point);
            onCurve = cb.value(t);
            dTmp = onCurve.distance(point);
            error += dTmp;
            if(d < dTmp) {
                d = dTmp;
                worstPoint = point;
            }
        }
        return new FitError(cb, d, error, worstPoint);
    }

    public Point worstPointFitted() {
        double t, d = 0, dTmp;
        Point onCurve, result = new Point();
        CubicBezier cb = fit();
        for(Point point: points) {
            t = parameterization.getParameter(point);
            onCurve = cb.value(t);
            dTmp = onCurve.distance(point);
//            System.out.println("t: " + t + ", " + "d:" + dTmp);
            if(d < dTmp) {
                d = dTmp;
                result = point;
            }
        }
        return result;
    }

    private class FitError {
        CubicBezier cb;
        double maxError;
        double totalError;
        Point worstFittedPoint;

        public FitError(CubicBezier cb, double maxError, double totalError, Point worstFittedPoint) {
            this.cb = cb;
            this.maxError = maxError;
            this.totalError = totalError;
            this.worstFittedPoint = worstFittedPoint;
        }
    }

    @Override
    public String toString() {
        return "PointsStrip{" +
                "points=" + points +
                '}';
    }
}
