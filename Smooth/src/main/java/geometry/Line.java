package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Created by oscar on 4/03/16.
 */
public class Line {
    private Vector2D p0;
    private Vector2D p1;
    private Vector2D v;
    private double A;
    private double B;

    public Line() {
        super();
        p0 = new Vector2D(0,0);
        p1 = new Vector2D(0,0);
        v = new Vector2D(0,0);
        A = B = 0;
    }

    public Line(Vector2D p0, Vector2D p1) {
        super();
        this.p0 = p0;
        this.p1 = p1;
        v = new Vector2D(p1.getX() - p0.getX(), p1.getY() - p0.getY());
        A = v.getY() / v.getX();
        B = p0.getY() - A * p0.getX();
    }

    public Vector2D getP0() {
        return p0;
    }

    public Vector2D getP1() {
        return p1;
    }

    public Vector2D getV() {
        return v;
    }

    public Vector2D intersection(Line l) {
        if (v.getX() != 0 && l.getV().getX() != 0) {
            double x = (l.B - B) / (A - l.A);
            double y = A * x + B;
            return new Vector2D(x, y);
        } else if (l.getV().getX() != 0) {
            double x = l.B;
            double y = 0;
            return new Vector2D(x, y);
        } else {
            double x = l.getP0().getX();
            double y = 0;
            return new Vector2D(x, y);
        }
    }

    public double perpendicularDistance(Vector2D p) {
        Line perpendicular = new Line(p, new Vector2D(v.getY(), -v.getX()));
        Vector2D intersectionPoint = intersection(perpendicular);

        return p.distance(intersectionPoint);
    }

    @Override
    public String toString() {
        return "Line{" +
                "p0=" + p0 +
                ", v=" + v +
                '}';
    }
}
