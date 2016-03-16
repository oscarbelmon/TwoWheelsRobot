package graphics;

import geometry.CubicBezier;
import geometry.Point;
import geometry.PointsStrip;

/**
 * Created by oscar on 12/2/16.
 */
public class Main {
    //    public static void main(String[] args) {
//        new MyOpenGLWindow("Robot");
//    }
    public static void main(String[] args) {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Point(-100, 100));
        ps.addPoint(new Point(100, 100));
        ps.addPoint(new Point(100, -100));
        ps.addPoint(new Point(-100, -100));
        CubicBezier cb = new CubicBezier(ps);
        System.out.println(cb.value(0.5));
        System.out.println(cb.value(0));
        System.out.println(cb.value(1));
        System.out.println(cb.value(0.32));
    }
}
