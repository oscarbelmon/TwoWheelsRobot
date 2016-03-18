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
        CubicWithLength cubicWithLength = filterCubic(s);
        return cubicWithLength.cubicBezier.value(cubicWithLength.t);
    }

    public Vector2D curvatureCenter(double s) {
        CubicWithLength cbl = filterCubic(s);
        return cbl.cubicBezier.curvatureCenter(cbl.t);
    }

    public double curvatureRadius(double s) {
        CubicWithLength cbl = filterCubic(s);
        return cbl.cubicBezier.curvatureRadius(s);
    }

    public Vector2D tangentNormalized(double s) {
        CubicWithLength cbl = filterCubic(s);
        return cbl.cubicBezier.firstDerivative(cbl.t).normalize();
    }

    public Vector2D normalNormalized(double s) {
        Vector2D v = tangentNormalized(s);
        return new Vector2D(v.getY(), -v.getX());
    }

    private CubicWithLength filterCubic(double s) {
        CubicBezier previous = cubics.get(new Double(0));
        double previousLength = 0;
        for(Double length: cubics.keySet()) {
            if(length > s) {
                CubicBezier cb = previous;
                double t = cb.inverse(s-previousLength);
                return new CubicWithLength(previous, t);
            }
            previous = cubics.get(length);
            previousLength = length;
        }
        return new CubicWithLength(null, 0);
    }

    private class CubicWithLength {
        CubicBezier cubicBezier;
        double t;

        CubicWithLength(CubicBezier cb, double t) {
            this.cubicBezier = cb;
            this.t = t;
        }
    }
}
