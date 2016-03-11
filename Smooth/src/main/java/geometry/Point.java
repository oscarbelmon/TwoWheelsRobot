package geometry;

/**
 * Created by oscar on 4/03/16.
 */
public class Point {
    private double x;
    private double y;

    public Point() {
        super();
        x = y = 0;
    }

    public Point(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Point p) {
        return Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y));
    }

    Point scale(double s) {
        return new Point(s*x, s*y);
    }

    public Point sum(Point p) {
        return new Point(x+p.x, y+p.y);
    }

    public Point substract(Point p) {
        return new Point(x-p.x, y-p.y);
    }

    public Point sum(Vector v) {
        return new Point(x+v.getVx(), y+v.getVy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        return Double.compare(point.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
