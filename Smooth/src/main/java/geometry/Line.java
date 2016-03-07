package geometry;

/**
 * Created by oscar on 4/03/16.
 */
public class Line {
    private Point p0;
    private Point p1;
    private Vector v;
    private double A;
    private double B;

    public Line() {
        super();
        p0 = new Point();
        p1 = new Point();
        v = new Vector();
        A = B = 0;
    }

    public Line(Point p0, Point p1) {
        super();
        this.p0 = p0;
        this.p1 = p1;
        v = new Vector(p1.getX() - p0.getX(), p1.getY() - p0.getY());
//        A = (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        A = v.getVy() / v.getVx();
        B = p0.getY() - A * p0.getX();
    }

    public Line(Point p, Vector v) {
        super();
        this.p0 = p;
        this.p1 = new Point(p.getX() + v.getVx(), p.getY() + v.getVy());
        this.v = v;
        A = v.getVy() / v.getVx();
        B = p0.getY() - A * p0.getX();
    }

    public Point getP0() {
        return p0;
    }

    public Point getP1() {
        return p1;
    }

    public Vector getV() {
        return v;
    }

    public Point intersection(Line l) {
//        System.out.println(l.getV().getVx());
        if (v.getVx() != 0 && l.getV().getVx() != 0) {
//            System.out.println("uno");
            double x = (l.B - B) / (A - l.A);
            double y = A * x + B;
            return new Point(x, y);
        } else if (l.getV().getVx() != 0) {
//            System.out.println("dos");
            double x = l.B;
            double y = 0;
            return new Point(x, y);
        } else {
//            System.out.println("tres");
            double x = l.getP0().getX();
            double y = 0;
            return new Point(x, y);
        }
    }

    public double perpendicularDistance(Point p) {
        Line perpendicular = new Line(p, v.perpendicular());
        Point intersectionPoint = intersection(perpendicular);

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
