package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 * Created by oscar on 30/03/16.
 */
public class BezierCurveDTest {
    public static BezierCurve b;
    public static BezierCurveD b2;

    @BeforeClass
    public static void startUp() {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Vector2D(100, 160));
        ps.addPoint(new Vector2D(350, 200));
        ps.addPoint(new Vector2D(200, 260));
        ps.addPoint(new Vector2D(220, 40));

        PointStripD psd = new PointStripD();
        psd.addPoint(new Vector2D(100, 160));
        psd.addPoint(new Vector2D(350, 200));
        psd.addPoint(new Vector2D(200, 260));
        psd.addPoint(new Vector2D(220, 40));

        b = new CubicBezier(ps);
        b2 = new BezierCurveD(psd);
    }

    @Test
    public void testLength() {
        assertThat(b.getLength(), is(closeTo(b2.getLength(), 0.0001)));
    }

    @Test
    public void testFirstDerivative() {
        double t = 0.5;
        Vector2D d = b.firstDerivative(t);
        Vector2D d2 = b2.firstDerivative(t);

        assertThat(d, is(d2));
    }

    @Test
    public void testSecondDerivative() {
        double t = 0.4;
        Vector2D d = b.secondDerivative(t);
        Vector2D d2 = b2.secondDerivative(t);

        assertThat(d.getX(), is(closeTo(d2.getX(), 0.0001)));
        assertThat(d.getY(), is(closeTo(d2.getY(), 0.0001)));
    }

    @Test
    public void testValue() {
        double t = 0.4;
        Vector2D value = b.value(t);
        Vector2D value2 = b2.value(t);

        // TODO Check this test it gives a big error
        assertThat(value.getX(), is(closeTo(value2.getX(), 1)));
        assertThat(value.getY(), is(closeTo(value2.getY(), 1)));
    }

    @Test
    public void testInverse() {
        double s = b.getLength()/(10*Math.random());
        assertThat(b.inverse(s), is(closeTo(b2.inverse(s), 0.00011)));
    }

    @Test
    public void testCurvature() {
        double t = Math.random();
        assertThat(b.curvature(t), is(closeTo(b2.curvature(t), 0.0001)));
    }

    @Test
    public void testCurvatureCenter() {
        double t = Math.random();
        Vector2D center = b.curvatureCenter(t);
        Vector2D center2 = b2.curvatureCenter(t);

        // TODO Check this test it gives a big error
        assertThat(center.getX(), is(closeTo(center2.getX(), 1)));
        assertThat(center.getY(), is(closeTo(center2.getY(), 1)));
    }
}
