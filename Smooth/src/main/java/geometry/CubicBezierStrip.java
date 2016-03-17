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
    private CubicBezier firstCubic;

    public CubicBezierStrip(List<CubicBezier> cubics) {
        super();
        calculateLengths(cubics);
    }

    private void calculateLengths(List<CubicBezier> cubics) {
        firstCubic = cubics.get(0);
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
        if(s == 0) return firstCubic.value(0);
        if(s == totalLength) return cubics.get(totalLength).value(1);
        CubicBezier previous = null;
        double previousLength = 0;
        for(Double length: cubics.keySet()) {
            if(length > s) {
//                CubicBezier cb = cubics.get(length);
                CubicBezier cb = previous;
                double t = cb.inverse(s-previousLength);
                return cb.value(t);
            }
            previous = cubics.get(length);
            previousLength = length;
        }

        return new Vector2D(0,0);
    }
}
