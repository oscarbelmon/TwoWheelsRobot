package robotics;

/**
 * Created by oscar on 18/03/16.
 */
public class TwoWheelsRobot {
    private double wheelsDistance;
    private double speed;
    private double differentialSpeed;

    public TwoWheelsRobot(double wheelsDistance, double speed) {
        this.wheelsDistance = wheelsDistance;
        this.speed = speed;
    }

    public double getDifferentialSpeed(double r) {
        return (speed * wheelsDistance/2) / r;
    }
}
