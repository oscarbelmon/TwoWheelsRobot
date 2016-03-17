package geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 17/3/16.
 */
public class CubicBezierStrip {
    private List<CubicBezier> cubics;
    private List<Double> lengths = new ArrayList<>();
    private double totalLength;

    public CubicBezierStrip(List<CubicBezier> cubics) {
        super();
        this.cubics = cubics;
        calculateLengths();
    }

    private void calculateLengths() {
        for(CubicBezier cb: cubics) {
//            lengths.add(cb.getLength());
        }
    }
}
