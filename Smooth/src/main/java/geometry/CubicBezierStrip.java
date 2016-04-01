package geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 30/03/16.
 */
public class CubicBezierStrip {
    private Map<Double, BezierCurve> cubics = new LinkedHashMap<>();
    private double totalLength = 0;

    public CubicBezierStrip(List<BezierCurve> cubics) {
        super();
        calculateLength(cubics);
    }

    private void calculateLength(List<BezierCurve> cubics) {
        double length = 0;
        BezierCurve tmp = null;
        for(BezierCurve cubic: cubics) {
            length = cubic.getLength();
            this.cubics.put(totalLength, cubic);
            totalLength += length;
            tmp = cubic;
        }
        this.cubics.put(totalLength, tmp);
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
        return cbl.cubicBezier.curvatureRadius(cbl.t);
    }

    public double curvature(double s) {
        CubicWithLength cbl = filterCubic(s);
        return cbl.cubicBezier.curvature(cbl.t);
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
        BezierCurve previous = cubics.get(new Double(0));
        double previousLength = 0;
        for(Double length: cubics.keySet()) {
            if(length > s) {
                BezierCurve bc = previous;
                double t = bc.inverse(s - previousLength);
                return new CubicWithLength(previous, t);
            }
            previous = cubics.get(length);
            previousLength = length;
        }
        return new CubicWithLength(null, 0);
    }

    private class CubicWithLength {
        BezierCurve cubicBezier;
        double t;

        CubicWithLength(BezierCurve cb, double t) {
            this.cubicBezier = cb;
            this.t = t;
        }
    }

}
