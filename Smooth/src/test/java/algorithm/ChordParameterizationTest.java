package algorithm;

import geometry.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 6/03/16.
 */
public class ChordParameterizationTest {
    @Test
    public void test1() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0,0));
        points.add(new Point(1,0));
        points.add(new Point(2,0));
        points.add(new Point(3,0));
        points.add(new Point(4,0));
        points.add(new Point(5,0));
        points.add(new Point(6,0));
        ChordParameterization cp = new ChordParameterization(points);
        System.out.println(cp.getParamter(new Point(0,0)));
        System.out.println(cp.getParamter(new Point(1,0)));
        System.out.println(cp.getParamter(new Point(2,0)));
        System.out.println(cp.getParamter(new Point(3,0)));
        System.out.println(cp.getParamter(new Point(4,0)));
        System.out.println(cp.getParamter(new Point(5,0)));
        System.out.println(cp.getParamter(new Point(6,0)));
    }
}
