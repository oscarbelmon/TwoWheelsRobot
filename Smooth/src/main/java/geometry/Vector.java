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

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public Vector perpendicular() {
        return new Vector(vy, -vx);
    }
}
