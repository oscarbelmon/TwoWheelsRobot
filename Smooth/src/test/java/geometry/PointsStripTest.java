package geometry;

import algorithm.ChordParameterization;
import algorithm.NewtonRaphsonParameterization;
import algorithm.Parameterization;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 9/3/16.
 */
public class PointsStripTest {
    @Test
    public void test2() {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Point(-100, 100));
        ps.addPoint(new Point(100, 100));
        ps.addPoint(new Point(100, -100));
        ps.addPoint(new Point(-100, -100));
        CubicBezier cb = new CubicBezier(ps);

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            points.add(cb.value(i/20.0));
        }
        ChordParameterization cp = new ChordParameterization(points);
        cp.showParameterization();
        NewtonRaphsonParameterization cp2 = new NewtonRaphsonParameterization(cp, cb);
        cp2.showParameterization();
    }

    @Test
    public void test4() {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Point(-100, 100));
        ps.addPoint(new Point(100, 100));
        ps.addPoint(new Point(100, -100));
        ps.addPoint(new Point(-100, -100));
        CubicBezier cb = new CubicBezier(ps);

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            points.add(cb.value(i/20.0));
        }

        Parameterization parameterization = new ChordParameterization(points);
        PointsStrip ps2 = new PointsStrip(points, parameterization);
        List<CubicBezier> cubics = ps2.fit(1000, new Vector(points.get(1), points.get(0)), new Vector(points.get(points.size()-1), points.get(points.size()-2)));
        for(CubicBezier cubic: cubics)
            System.out.println(cubic);
    }
}
