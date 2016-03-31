package graphics;

import algorithm.ChordParameterization;
import algorithm.Parameterization;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import connection.BTConnection;
import geometry.*;
import graphics.myopengl.OpenGLWindow;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import robotics.TwoWheelsRobot;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 31/03/16.
 */
public class MyOpenGLWindowD extends OpenGLWindow {
    private PointsStrip pointsStrip = new PointsStrip();
    private int cnt = 0;
//    private List<CubicBezier> cubics = new ArrayList<>();
//    private CubicBezierStrip cbs;
        private List<BezierCurveD> cubics = new ArrayList<>();
    private CubicBezierStripD cbs;
    private BTConnection btConnection = new BTConnection();
    private JTextArea jtaDevices;
    private JTextField jtfDeviceSelected;
    private JSlider jsSpeed;
    private boolean animation = false;
    private int iterations = 0;
    private int iteration = 0;
    private double totalLength = 0;
    private TwoWheelsRobot robot = new TwoWheelsRobot(0.135);
    private MyOpenGLWindow other;

    public MyOpenGLWindowD(String title, MyOpenGLWindow other) {
        super(title);
        this.other = other;
    }

    @Override
    protected JPanel infoPanel() {
        return createInfoPanel();
    }

    private JPanel createInfoPanel() {
        JButton jbSimulateCurve = new JButton("Simulate");
        jbSimulateCurve.addActionListener(e -> simulateCurve());
        JButton jbFitCurve = new JButton("Fit curve");
        jbFitCurve.addActionListener(e -> fitCurve());
        JButton jbCleanCanvas = new JButton("Clean canvas");
        jbCleanCanvas.addActionListener(e -> cleanCanvas());
        JButton jbShowInfo = new JButton("Show info");
        jbShowInfo.addActionListener(e -> showInfo());
        JButton jbStartAnimation = new JButton("Start animation");
        jbStartAnimation.addActionListener(e -> startAnimation());
        JButton jbStopAnimation = new JButton("Stop animation");
        jbStopAnimation.addActionListener(e -> stopAnimation());
        jsSpeed = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JButton jbStartDiscovery = new JButton("Start discovery");
        jbStartDiscovery.addActionListener(e -> startDiscovery());
        jtaDevices = new JTextArea(10, 40);
        jtfDeviceSelected = new JTextField(2);
        JButton jbConnectDevide = new JButton("Connect");
        jbConnectDevide.addActionListener(e -> connectDevice());

        JPanel jpPanel = new JPanel();
        jpPanel.setLayout(new BoxLayout(jpPanel, BoxLayout.PAGE_AXIS));
        jpPanel.add(jbSimulateCurve);
        jpPanel.add(jbFitCurve);
        jpPanel.add(jbCleanCanvas);
        jpPanel.add(jbShowInfo);
        jpPanel.add(jbStartAnimation);
        jpPanel.add(jbStopAnimation);
        jpPanel.add(jsSpeed);
        jpPanel.add(jbStartDiscovery);
        jpPanel.add(jtaDevices);
        jpPanel.add(jtfDeviceSelected);
        jpPanel.add(jbConnectDevide);

        return jpPanel;
    }

    private void stopAnimation() {
        iteration = iterations;
    }

    private void startAnimation() {
        animation = true;
        iterations = (int) cbs.getTotalLength() / 10;
        iteration = 0;
        totalLength = cbs.getTotalLength();
        new Thread(new Hilo()).start();
//        new Thread(new Hilo2()).start();
    }

    private void connectDevice() {
        String result = btConnection.connect(new Integer(jtfDeviceSelected.getText()).intValue());
        jtaDevices.append(result + "\n");
    }

    private void startDiscovery() {
        btConnection.bluetoothDiscovery();
        List<BTConnection.Device> devices = btConnection.getBtDevices();
        for (int i = 0; i < devices.size(); i++) {
            jtaDevices.append(i + ".- " + btConnection.getFriendlyName(i));
            jtaDevices.append("\n");
        }
    }

    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        if (animation) {
            renderAnimation(gl);
        } else if (!cubics.isEmpty()) {
            gl.glColor3d(1, 0, 0);
//            for (CubicBezier cubic : cubics)
//                bezier(gl, cubic);
            for(BezierCurveD cubic: cubics)
                bezier(gl, cubic);
//            bezierS(gl);
        } else {
            gl.glColor3d(0, 1, 0);
            renderAllPoints(gl);
        }
        gl.glPopMatrix();
    }

    private void renderAnimation(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glPushMatrix();
        // The curves
        gl.glColor3d(1, 0, 0);
//        for (CubicBezier cubic : cubics)
//            bezier(gl, cubic);
        for(BezierCurveD cubic: cubics)
                bezier(gl, cubic);

        // The point
        gl.glColor3d(0, 0, 0);
        gl.glBegin(GL2.GL_POINTS);
        Vector2D point;
        point = cbs.inverse(totalLength * iteration / iterations);
        gl.glVertex2d(point.getX(), point.getY());
//        point = cbs.inverse(totalLength);
//        gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void bezierS(GL2 gl) {
        gl.glColor3d(0, 1, 0);
        gl.glPointSize(5);
        gl.glBegin(GL2.GL_POINTS);
        double totalLength = cbs.getTotalLength();
        Vector2D point;
        int iterations = (int) cbs.getTotalLength() / 10;
        for (int i = 0; i < iterations; i++) {
            point = cbs.inverse(totalLength * i / iterations);
            gl.glVertex2d(point.getX(), point.getY());

        }
        point = cbs.inverse(totalLength);
        gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();

        // Normals
//        gl.glBegin(GL2.GL_LINES);
//        Vector2D point2;
//        double dSpeed;
//        for(int i = 0; i < iterations; i++) {
//            point = cbs.inverse(totalLength*i/iterations);
//            point2 = cbs.curvatureCenter(totalLength*i/iterations);
//            dSpeed = robot.getDifferentialSpeed(cbs.curvatureRadius(totalLength*i/iterations));
//            System.out.println(dSpeed);
//            gl.glVertex2d(point.getX(), point.getY());
//            gl.glVertex2d(point2.getX(), point2.getY());
//        }
//        gl.glEnd();

    }

//    private void bezier(GL2 gl, CubicBezier cb) {
    private void bezier(GL2 gl, BezierCurveD cb) {
        Vector2D p;
        double steps = 30.0;
        gl.glColor3d(1, 0, 0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for (int i = 0; i <= steps; i++) {
            p = cb.value(i / steps);
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();

        // End points
//        gl.glPointSize(3);
//        gl.glColor3d(0, 0, 0);
//        gl.glBegin(GL.GL_POINTS);
//        p = cb.value(0);
//        gl.glVertex2d(p.getX(), p.getY());
//        p = cb.value(1);
//        gl.glVertex2d(p.getX(), p.getY());
//        gl.glEnd();

        // Tangents
//        gl.glColor3d(0, 0, 1);
//        gl.glBegin(GL2.GL_LINES);
//        Vector2D start = cb.value(0);
//        Vector2D vector = cb.firstDerivative(0).normalize().scalarMultiply(20);
//        Vector2D end = start.add(vector);
//        gl.glVertex2d(start.getX(), start.getY());
//        gl.glVertex2d(end.getX(), end.getY());
//        start = cb.value(1);
//        vector = cb.firstDerivative(1).normalize().scalarMultiply(-20);
//        end = start.add(vector);
//        gl.glVertex2d(start.getX(), start.getY());
//        gl.glVertex2d(end.getX(), end.getY());
//        gl.glEnd();
    }

//    private void renderTangents(GL2 gl) {
//        if (pointsStrip.size() < 2) return;
//        Vector2D origin = pointsStrip.getTangentNormalizedAtStart().scalarMultiply(20);
//        Vector2D destination = pointsStrip.get(0).add(origin);
//        gl.glColor3d(0, 1, 0);
//        gl.glBegin(GL.GL_LINES);
//        gl.glVertex2d(pointsStrip.get(0).getX(), pointsStrip.get(0).getY());
//        gl.glVertex2d(destination.getX(), destination.getY());
//        gl.glEnd();
//    }


//    private void renderFilteredPoints(GL2 gl) {
//        PointsStrip filtered = new DouglassPeucker(pointsStrip).simplify(5);
//        gl.glColor3d(1, 0, 0);
//        gl.glBegin(GL.GL_LINE_STRIP);
//        for (Vector2D point : filtered.getPoints())
//            gl.glVertex2d(point.getX(), point.getY());
//        gl.glEnd();
//    }

    private void renderAllPoints(GL2 gl) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Vector2D point : pointsStrip.getPoints())
            gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    @Override
    public void lights(GL2 gl) {
    }

    @Override
    public void init(GL2 gl) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - getWidth() / 2;
        int y = getHeight() / 2 - e.getY();
        pointsStrip.addPoint(new Vector2D(x, y));
        display();
    }

    private void showInfo() {
        System.out.println(cubics.size());
//        for (CubicBezier cubicBezier : cubics)
        for (BezierCurveD cubicBezier : cubics)
            System.out.println(cubicBezier);
    }

    private void cleanCanvas() {
        pointsStrip = new PointsStrip();
        cubics = new ArrayList<>();
        display();
    }

    private void fitCurve() {
        other.setPoints(pointsStrip.getPoints());
        pointsStrip = pointsStrip.removeDuplicates();
        Parameterization parameterization = new ChordParameterization(pointsStrip.getPoints());
//        PointsStrip ps = new PointsStrip(pointsStrip.getPoints(), parameterization);
//        cubics = ps.fit(20, pointsStrip.get(1).subtract(pointsStrip.get(0)).normalize(), pointsStrip.get(pointsStrip.size() - 2).subtract(pointsStrip.get(pointsStrip.size() - 1)).normalize());
        PointStripD ps = new PointStripD(pointsStrip.getPoints(), parameterization);
        cubics = ps.fit(20, pointsStrip.get(1).subtract(pointsStrip.get(0)).normalize(), pointsStrip.get(pointsStrip.size() - 2).subtract(pointsStrip.get(pointsStrip.size() - 1)).normalize());
        System.out.println("Points: " + pointsStrip.size());
        System.out.println("Cubics: " + cubics.size());
        System.out.println("Points in cubics: " + ((cubics.size() - 1) * 3 + 4));
//        cbs = new CubicBezierStrip(cubics);
        cbs = new CubicBezierStripD(cubics);
        display();
    }

    private void simulateCurve() {
        pointsStrip = new PointsStrip();
        cubics = new ArrayList<>();
        double radius = 200, angle;
        for (int i = 0; i < 100; i++) {
            angle = Math.PI * i / 180.0;
            pointsStrip.addPoint(new Vector2D(radius * Math.cos(angle), radius * Math.sin(angle)));
        }
        display();
    }

    private class Hilo implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while (iteration < iterations) {
                try {
//                    Thread.sleep(jsSpeed.getValue());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cbs.inverse(totalLength * iteration / iterations);
                double radius = cbs.curvatureRadius(totalLength * iteration / iterations)/200.0;
                double differentialSpeed = robot.getDifferentialSpeed(radius, jsSpeed.getValue());
                System.out.println("Differential speed: " + differentialSpeed);
                double speed = jsSpeed.getValue();
                String command = (int)(speed+differentialSpeed)+","+ (int)(speed-differentialSpeed);
                System.out.println(command);
//                btConnection.sendCommand(command);
                btConnection.sendTest("" + (int)(14*(speed-differentialSpeed)));
                btConnection.sendTest("" + (int)(14*(speed+differentialSpeed)));
                iteration++;
                display();
            }
//            btConnection.sendCommand("0,0");
            btConnection.sendTest(""+0);
            btConnection.sendTest(""+0);
            System.out.println("Simulation time: " + (System.currentTimeMillis() - start)/1000.0);
            animation = false;
        }
    }
}
