package algorithm;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * Created by oscar on 6/03/16.
 */
public class ChordParameterization extends Parameterization {
    public ChordParameterization(List<Vector2D> points) {
        super(points);
        this.points = points;
        parameterize();
    }

    public void parameterize() {
        parameterizedPoints.put(points.get(0), new Double(0));
        parameterizedPoints.put(points.get(points.size()-1), new Double(1));
        double totalLength = totalLength(points);
        double length = 0;
        for(int i = 1; i < points.size()-1; i++) {
            length += points.get(i).distance(points.get(i-1));
            parameterizedPoints.put(points.get(i), new Double(length/totalLength));
        }
    }

    private double totalLength(List<Vector2D> points) {
        double length = 0;
        for(int i = points.size()-1; i > 0; i--)
            length += points.get(i).distance(points.get(i-1));
        return length;
    }
}
