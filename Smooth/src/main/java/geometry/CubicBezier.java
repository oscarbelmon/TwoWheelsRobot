package geometry;

import java.util.List;

/**
 * Created by oscar on 5/03/16.
 */
public class CubicBezier {
    List<Point> points;
    private Vector V2_V1;
    private Vector V1_V0;
    private Vector V3_V2;
    Point V0_2V1V2;
    Point V1_2V2V3;

    public CubicBezier(List<Point> points) {
        super();
        if(points.size() != 4) throw new IllegalArgumentException("The number of points must be 4");
        this.points = points;
//        V2_V1 = points.get(2).substract(points.get(1));
//        V1_V0 = points.get(1).substract(points.get(0));
//        V3_V2 = points.get(3).substract(points.get(2));
        V2_V1 = new Vector(points.get(2), points.get(1));
        V1_V0 = new Vector(points.get(1), points.get(0));
        V3_V2 = new Vector(points.get(3), points.get(2));
        V0_2V1V2 = points.get(0)
                .substract(points.get(1).scale(2))
//                .scale(2)
                .sum(points.get(2));
        V1_2V2V3 = points.get(1)
                .substract(points.get(2).scale(2))
//                .scale(2)
                .sum(points.get(3));
    }

    public Point value(double t) {
        double B0 = (1-t)*(1-t)*(1-t);
        double B1 = 3*t*(1-t)*(1-t);
        double B2 = 3*t*t*(1-t);
        double B3 = t*t*t;
        return points.get(0).scale(B0)
                .sum(points.get(1).scale(B1))
                .sum(points.get(2).scale(B2))
                .sum(points.get(3).scale(B3));
    }

    public Vector firstDerivative(double t) {
        double B0 = (1-t)*(1-t);
        double B1 = 2*t*(1-t);
        double B2 = t*t;
        return V1_V0.scale(B0)
                .sum(V2_V1.scale(B1))
                .sum(V3_V2.scale(B2))
                .scale(3);
    }

    public Vector secondDerivative(double t) {
        double B0 = 1-t;
        double B1 = t;
        return new Vector(V0_2V1V2.scale(B0)
                .sum(V1_2V2V3).scale(B1)
                .scale(6));
    }

    public double curvature(double t) {
        Vector vp = firstDerivative(t);
//        System.out.println(vp);
        Vector vpp = secondDerivative(t);
        double numerator = vp.crossProductZ(vpp);
        double denominator = Math.pow(vp.module(), 3);
        return numerator/denominator;
    }

    public double curvatureRadius(double t) {
        return 1/curvature(t);
    }

    public Point curvatureCenter(double t) {
        Vector vp = firstDerivative(t);
        double curvatureRadius = curvatureRadius(t);
        Vector vpPerpendicular = vp.perpendicular().normalize().scale(-curvatureRadius);
        Point p = value(t);
        return p.sum(vpPerpendicular);
    }
}
