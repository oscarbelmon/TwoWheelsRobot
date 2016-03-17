package algorithm;

import geometry.Point;
import geometry.PointsStrip;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by oscar on 4/03/16.
 */
public class DouglassPeuckerTest {
    @Test
    public void test1() {
//        List<Point> points = new ArrayList<>();
//        points.add(new Point());
//        points.add(new Point(5,5));
//        points.add(new Point(9,3));
//        points.add(new Point(12,2));
//        points.add(new Point(14,4));
//        points.add(new Point(18,3));
//        points.add(new Point(22,-2));
//        points.add(new Point(29,8));
//        points.add(new Point(32,3));
//        points.add(new Point(37,3));
        PointsStrip points = new PointsStrip();
        points.addPoint(new Vector2D(0,0));
        points.addPoint(new Vector2D(5,5));
        points.addPoint(new Vector2D(9,3));
        points.addPoint(new Vector2D(12,2));
        points.addPoint(new Vector2D(14,4));
        points.addPoint(new Vector2D(18,3));
        points.addPoint(new Vector2D(22,-2));
        points.addPoint(new Vector2D(29,8));
        points.addPoint(new Vector2D(32,3));
        points.addPoint(new Vector2D(37,3));


        DouglassPeucker douglassPeucker = new DouglassPeucker(points);
//        List<Point> result = douglassPeucker.simplify(6);
        PointsStrip result = douglassPeucker.simplify(6);
//        for(Point p: result)
//            System.out.println(p);
        assertThat(result.size(), is(2));

        result = douglassPeucker.simplify(0.01);
        assertThat(result.size(), is(points.size()));
    }
}
