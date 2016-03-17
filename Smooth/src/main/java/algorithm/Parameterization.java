package algorithm;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 10/3/16.
 */
public abstract class Parameterization {
    protected Map<Vector2D, Double> parameterizedPoints = new HashMap<>();
    protected List<Vector2D> points;

    public abstract void parameterize();

    protected Parameterization(List<Vector2D> points) {
        this.points = points;
    }

    public List<Vector2D> getPoints() {
        return points;
    }

    public double getParameter(Vector2D p) {
        if(parameterizedPoints.get(p) == null) System.out.println("Maaaaaaaal");
        return parameterizedPoints.get(p).doubleValue();
    }

    public void showParameterization() {
        for(Vector2D point: points)
            System.out.println(parameterizedPoints.get(point));
    }
}
