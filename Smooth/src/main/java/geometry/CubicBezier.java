package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * Created by oscar on 5/03/16.
 */
public class CubicBezier extends BezierCurve {
    private PointsStrip points;
    private Vector2D V2_V1;
    private Vector2D V1_V0;
    private Vector2D V3_V2;
    private Vector2D V0_2V1V2;
    private Vector2D V1_2V2V3;

    public CubicBezier(PointsStrip points) {
        super(points.getPoints());
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

    public Vector2D secondDerivative(double t) {
        double B0 = 1-t;
        double B1 = t;
        return V0_2V1V2.scalarMultiply(B0)
                .add(V1_2V2V3.scalarMultiply(B1))
                .scalarMultiply(6);
    }

    @Override
    public String toString() {
        return "CubicBezier{" +
                points +
                '}';
    }
}
