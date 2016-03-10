package geometry;

/**
 * Created by oscar on 4/03/16.
 */
public class Vector {
    private double vx;
    private double vy;

    public Vector() {
        super();
        vx = vy = 0;
    }

    public Vector(double vx, double vy) {
        super();
        this.vx = vx;
        this.vy = vy;
    }

    public Vector(Point destination, Point origin) {
        vx = destination.getX() - origin.getX();
        vy = destination.getY() - origin.getY();
    }

    public Vector(Point p) {
        vx = p.getX();
        vy = p.getY();
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public Vector perpendicular() {
        return new Vector(vy, -vx);
    }

//    public double module() {
//        return Math.sqrt(vx*vx + vy*vy);
//    }

    public double module() {
        return Math.sqrt(dotProduct(this));
    }

    public double dotProduct(Vector v) {
        return vx*v.vx + vy*v.vy;
    }

    public double crossProductZ(Vector v) {
        return vx*v.vy - vy*v.vx;
    }

    public Vector normalize() {
        double module = module();
        return new Vector(vx/module, vy/module);
    }

    public Vector scale(double s) {
        return new Vector(s*vx, s*vy);
    }

    public Vector sum(Vector v) {
        return new Vector(vx+v.vx, vy+v.vy);
    }

    @Override
    public String toString() {
        return "Vector{" +
                vx + ", " +
                vy +
                '}';
    }
}
