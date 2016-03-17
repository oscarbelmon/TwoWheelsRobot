package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * Created by oscar on 5/03/16.
 */
public class CubicBezier {
    private PointsStrip points;
    private Vector2D V2_V1;
    private Vector2D V1_V0;
    private Vector2D V3_V2;
    private Vector2D V0_2V1V2;
    private Vector2D V1_2V2V3;

    public CubicBezier(PointsStrip points) {
        super();
        if(points.size() != 4) throw new IllegalArgumentException("The number of points must be 4");
        this.points = points;
        V2_V1 = points.get(2).subtract(points.get(1));
        V1_V0 = points.get(1).subtract(points.get(0));
        V3_V2 = points.get(3).subtract(points.get(2));
        V0_2V1V2 = points.get(0).subtract(points.get(1))
                .add(points.get(2).subtract(points.get(1)));
        V1_2V2V3 = points.get(1).subtract(points.get(2))
                .add(points.get(3).subtract(points.get(2)));
    }

    public Vector2D value(double t) {
        double B0 = (1-t)*(1-t)*(1-t);
        double B1 = 3*t*(1-t)*(1-t);
        double B2 = 3*t*t*(1-t);
        double B3 = t*t*t;
        return points.get(0).scalarMultiply(B0)
                .add(points.get(1).scalarMultiply(B1))
                .add(points.get(2).scalarMultiply(B2))
                .add(points.get(3).scalarMultiply(B3));
    }

    public Vector2D firstDerivative(double t) {
        double B0 = (1-t)*(1-t);
        double B1 = 2*t*(1-t);
        double B2 = t*t;
        return V1_V0.scalarMultiply(B0)
                .add(V2_V1.scalarMultiply(B1))
                .add(V3_V2.scalarMultiply(B2))
                .scalarMultiply(3);
    }

    public Vector2D secondDerivative(double t) {
        double B0 = 1-t;
        double B1 = t;
        return V0_2V1V2.scalarMultiply(B0)
                .add(V1_2V2V3.scalarMultiply(B1))
                .scalarMultiply(6);
    }

    public double curvature(double t) {
        Vector2D vp = firstDerivative(t);
        Vector2D vpp = secondDerivative(t);
        double numerator = vp.crossProduct(vp, vpp);
        double denominator = Math.pow(vp.getNorm(), 3);
        return numerator/denominator;
    }

    public double curvatureRadius(double t) {
        return 1/curvature(t);
    }

    public Vector2D curvatureCenter(double t) {
        Vector2D vp = firstDerivative(t);
        double curvatureRadius = curvatureRadius(t);
        Vector2D vpPerpendicular = new Vector2D(vp.getY(), vp.getX()).normalize().scalarMultiply(-curvatureRadius);
        Vector2D p = value(t);
        return p.add(vpPerpendicular);
    }

    public Vector2D getPoint(int index) {
        return points.getPoints().get(index);
    }

    @Override
    public String toString() {
        return "CubicBezier{" +
                points +
                '}';
    }
}
