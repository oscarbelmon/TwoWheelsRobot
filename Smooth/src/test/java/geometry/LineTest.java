package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by oscar on 4/03/16.
 */
public class LineTest {
    @Test
    public void test1() {
//        Line l1 = new Line(new Point(0, 0), new Point(1, 0));
//        Line l2 = new Line(new Point(1, -1), new Point(1, 1));
        Line l1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line l2 = new Line(new Vector2D(1, -1), new Vector2D(1, 1));
//        assertThat(l1.intersection(l2), is(new Point(1,0)));
        assertThat(l1.intersection(l2), is(new Vector2D(1,0)));
    }

    @Test
    public void test2() {
//        Line l1 = new Line(new Point(1,2), new Point (2,3));
//        Line l2 = new Line(new Point(3,2), new Point(5,6));
        Line l1 = new Line(new Vector2D(1,2), new Vector2D (2,3));
        Line l2 = new Line(new Vector2D(3,2), new Vector2D(5,6));
//        assertThat(l1.intersection(l2), is(new Point(5,6)));
        assertThat(l1.intersection(l2), is(new Vector2D(5,6)));
    }

    @Test
    public void test3() {
//        Line l1 = new Line(new Point(1,1), new Point (6,2));
//        Line l2 = new Line(new Point(1,6), new Point(6,-1));
        Line l1 = new Line(new Vector2D(1,1), new Vector2D (6,2));
        Line l2 = new Line(new Vector2D(1,6), new Vector2D(6,-1));
//        assertThat(l1.intersection(l2), is(new Point(5,6)));
//        assertThat(l1.intersection(l2), is(new Vector2D(5,6)));
    }
}
