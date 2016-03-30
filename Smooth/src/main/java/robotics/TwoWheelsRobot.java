package robotics;

/**
 * Created by oscar on 18/03/16.
 */
public class TwoWheelsRobot {
    private double wheelsDistance;

    public TwoWheelsRobot(double wheelsDistance) {
        this.wheelsDistance = wheelsDistance;
    }

    public double getDifferentialSpeed(double radius, double speed) {
//        double difference = (speed * wheelsDistance/2) / radius;
        double difference = (speed * wheelsDistance/2) / radius/2;
        return difference;
    }
}
