package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 * Created by oscar on 30/03/16.
 */
public class CubicBezierStripDTest {
    private static CubicBezierStrip cs;
    private static CubicBezierStripD cs2;
    private double s;

    @BeforeClass
    public static void startUp() {
        List<CubicBezier> list = new ArrayList<>();
        List<BezierCurveD> list2 = new ArrayList<>();

        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Vector2D(100, 160));
        ps.addPoint(new Vector2D(350, 200));
        ps.addPoint(new Vector2D(200, 260));
        ps.addPoint(new Vector2D(220, 40));

        list.add(new CubicBezier(ps));
        list2.add(new BezierCurveD(ps));

        ps = new PointsStrip();
        ps.addPoint(new Vector2D(220, 40));
        ps.addPoint(new Vector2D(200, 260));
        ps.addPoint(new Vector2D(450, 300));
        ps.addPoint(new Vector2D(300, 360));

        list.add(new CubicBezier(ps));
        list2.add(new BezierCurveD(ps));

        cs = new CubicBezierStrip(list);
        cs2 = new CubicBezierStripD(list2);
    }

    @Before
    public void setS() {
        double s = cs.getTotalLength()*Math.random();
    }

    @Test
    public void testGetTotalLength() {
        assertThat(cs.getTotalLength(), is(cs2.getTotalLength()));
    }

    @Test
    public void testInverse() {
        Vector2D v = cs.inverse(s);
        Vector2D v2 = cs2.inverse(s);

        assertThat(v.getX(), is(closeTo(v2.getX(), 1)));
        assertThat(v.getY(), is(closeTo(v2.getY(), 1)));
        assertEquals(v.getX(), v2.getX(), 1);
        assertEquals(v.getY(), v2.getY(), 1);
    }

    @Test
    public void testCurvatureCenter() {
        Vector2D v = cs.curvatureCenter(s);
        Vector2D v2 = cs2.curvatureCenter(s);

        assertThat(v.getX(), is(closeTo(v2.getX(), 1)));
        assertThat(v.getY(), is(closeTo(v2.getY(), 1)));
        assertEquals(v.getX(), v2.getX(), 1);
        assertEquals(v.getY(), v2.getY(), 1);
    }

    @Test
    public void testCurvatureRadius() {
        assertThat(cs.curvatureRadius(s), is(closeTo(cs2.curvatureRadius(s), 0.0001)));;
    }

    @Test
    public void testTangentNormalized() {
        Vector2D v = cs.tangentNormalized(s);
        Vector2D v2 = cs2.tangentNormalized(s);

        assertThat(v.getX(), is(closeTo(v2.getX(), 1)));
        assertThat(v.getY(), is(closeTo(v2.getY(), 1)));
        assertEquals(v.getX(), v2.getX(), 1);
        assertEquals(v.getY(), v2.getY(), 1);
    }

    @Test
    public void testNormalNormalized() {
        Vector2D v = cs.normalNormalized(s);
        Vector2D v2 = cs2.normalNormalized(s);

        assertThat(v.getX(), is(closeTo(v2.getX(), 1)));
        assertThat(v.getY(), is(closeTo(v2.getY(), 1)));
        assertEquals(v.getX(), v2.getX(), 1);
        assertEquals(v.getY(), v2.getY(), 1);
    }
}
