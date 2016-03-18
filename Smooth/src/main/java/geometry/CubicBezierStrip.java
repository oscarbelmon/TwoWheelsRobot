package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 17/3/16.
 */
public class CubicBezierStrip {
    private Map<Double, CubicBezier> cubics = new LinkedHashMap<>();
    private double totalLength = 0;

    public CubicBezierStrip(List<CubicBezier> cubics) {
        super();
        calculateLengths(cubics);
    }

    private void calculateLengths(List<CubicBezier> cubics) {
        double length = 0;
        CubicBezier coso = null;
        for(CubicBezier cb: cubics) {
            length = cb.getLength();
            this.cubics.put(totalLength, cb);
            totalLength += length;
            coso = cb;
        }
        this.cubics.put(totalLength, coso);
    }

    public double getTotalLength() {
        return totalLength;
    }

    public Vector2D inverse(double s) {
        if(s == 0) return cubics.get(new Double(0)).value(s);
        if(s == totalLength) return cubics.get(totalLength).value(1);
        CubicWithLength cb = filterCubic(s);
        double t = cb.cb.inverse(s-cb.length);
        return cb.cb.value(t);
    }

    private CubicWithLength filterCubic(double s) {
        CubicBezier previous = cubics.get(new Double(0));
        double previousLength = 0;
        for(Double length: cubics.keySet()) {
            if(length > s) {
                CubicBezier cb = previous;
                double t = cb.inverse(s-previousLength);
                return new CubicWithLength(previous, previousLength);
            }
            previous = cubics.get(length);
            previousLength = length;
        }
        return new CubicWithLength(null, 0);
    }

    private class CubicWithLength {
        CubicBezier cb;
        double length;

        CubicWithLength(CubicBezier cb, double length) {
            this.cb = cb;
            this.length = length;
        }
    }
}
