package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.number.IsCloseTo.*;
/**
 * Created by oscar on 16/03/16.
 */
public class BezierCurveTest {
    @Test
    public void lengthTest() {
        List<Vector2D> poiints = new ArrayList<>();
        poiints.add(new Vector2D(120, 160));
        poiints.add(new Vector2D(35, 200));
        poiints.add(new Vector2D(220, 260));
        poiints.add(new Vector2D(220, 40));
        BezierCurve bc = new BezierCurve(poiints);
        assertThat(bc.getLength(), closeTo(272.87, 0.01));
    }
}
