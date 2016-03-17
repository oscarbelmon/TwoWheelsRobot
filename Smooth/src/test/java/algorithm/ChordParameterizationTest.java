package algorithm;

import geometry.Point;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 6/03/16.
 */
public class ChordParameterizationTest {
    @Test
    public void test1() {
        List<Vector2D> points = new ArrayList<>();
        points.add(new Vector2D(0,0));
        points.add(new Vector2D(1,0));
        points.add(new Vector2D(2,0));
        points.add(new Vector2D(3,0));
        points.add(new Vector2D(4,0));
        points.add(new Vector2D(5,0));
        points.add(new Vector2D(6,0));
        ChordParameterization cp = new ChordParameterization(points);
        System.out.println(cp.getParameter(new Vector2D(0,0)));
        System.out.println(cp.getParameter(new Vector2D(1,0)));
        System.out.println(cp.getParameter(new Vector2D(2,0)));
        System.out.println(cp.getParameter(new Vector2D(3,0)));
        System.out.println(cp.getParameter(new Vector2D(4,0)));
        System.out.println(cp.getParameter(new Vector2D(5,0)));
        System.out.println(cp.getParameter(new Vector2D(6,0)));
    }
}
