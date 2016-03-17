package graphics;

import algorithm.ChordParameterization;
import algorithm.DouglassPeucker;
import algorithm.NewtonRaphsonParameterization;
import algorithm.Parameterization;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import geometry.*;
import graphics.myopengl.OpenGLWindow;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/2/16.
 */
public class MyOpenGLWindow extends OpenGLWindow {
    private PointsStrip pointsStrip = new PointsStrip();
    private int cnt = 0;
    private List<CubicBezier> cubics = new ArrayList<>();
    private CubicBezierStrip cbs;

    public MyOpenGLWindow(String title) {
        super(title);
    }

    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
//        if(cnt == 4) bezier(gl);
//        renderAllPoints(gl);
//        renderFilteredPoints(gl);
//        renderTangents(gl);
//        if(cubics != null)
        if(!cubics.isEmpty()) {
            gl.glColor3d(1, 0, 0);
            bezierS2(gl);
            for (CubicBezier cubic : cubics)
                bezier2(gl, cubic);
        }
//        else {
////            bezier2(gl, model);
        else {
            gl.glColor3d(0,1,0);
            renderAllPoints(gl);
        }
//        }
//        coso(gl);
//        coso2(gl);
        gl.glPopMatrix();
    }

    private void bezierS(GL2 gl) {
        gl.glColor3d(0,1,0);
        gl.glPointSize(3);
        gl.glBegin(GL2.GL_POINTS);
        BezierCurve bc = cubics.get(0);
        double inverse, length = bc.getLength();
        Vector2D point;
        for(int i = 0; i <= 10; i++) {
            inverse = bc.inverse(i/10.*length);
            point = bc.value(inverse);
            gl.glVertex2d(point.getX(), point.getY());
        }
        gl.glEnd();
    }

    private void bezierS2(GL2 gl) {
        gl.glColor3d(0,1,0);
        gl.glPointSize(5);
        gl.glBegin(GL2.GL_POINTS);
        double totalLength = cbs.getTotalLength();
        Vector2D point;
        int iterations = (int)cbs.getTotalLength()/50;
        for(int i = 0; i < iterations; i++) {
            point = cbs.inverse(totalLength*i/iterations);
            gl.glVertex2d(point.getX(), point.getY());
        }
        point = cbs.inverse(totalLength);
        gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    private void coso2(GL2 gl) {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Vector2D(-100, 100));
        ps.addPoint(new Vector2D(100, 100));
        ps.addPoint(new Vector2D(100, -100));
        ps.addPoint(new Vector2D(-100, -100));
        CubicBezier cb = new CubicBezier(ps);
        gl.glColor3d(0,1,0);
        bezier2(gl, cb);

        List<Vector2D> points = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            points.add(cb.value(i/20.0));
        }

        Parameterization parameterization = new ChordParameterization(points);
        PointsStrip ps2 = new PointsStrip(points, parameterization);
        List<CubicBezier> cubics = ps2.fit(10, points.get(1).subtract(points.get(0)),
                points.get(points.size()-1).subtract(points.get(points.size()-2)));
        gl.glColor3d(1,0,0);
        for(CubicBezier cubic: cubics)
            bezier2(gl, cubic);
    }

    private void bezier2(GL2 gl, CubicBezier cb) {
        Vector2D p;
        double steps = 30.0;
        gl.glColor3d(1, 0, 0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(int i = 0; i <= steps; i++) {
            p = cb.value(i/steps);
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

    private void renderTangents(GL2 gl) {
        if(pointsStrip.size() < 2) return;
        Vector2D origin = pointsStrip.getTangentNormalizedAtStart().scalarMultiply(20);
        Vector2D destination = pointsStrip.get(0).add(origin);
        gl.glColor3d(0, 1, 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2d(pointsStrip.get(0).getX(), pointsStrip.get(0).getY());
        gl.glVertex2d(destination.getX(), destination.getY());
        gl.glEnd();
    }


    private void renderFilteredPoints(GL2 gl) {
        PointsStrip filtered = new DouglassPeucker(pointsStrip).simplify(5);
        gl.glColor3d(1, 0, 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for(Vector2D point: filtered.getPoints())
            gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    private void renderAllPoints(GL2 gl) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for(Vector2D point: pointsStrip.getPoints())
            gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    @Override
    public void lights(GL2 gl) {
    }

    @Override
    public void init(GL2 gl) {
    }

    private void bezier(GL2 gl) {
        CubicBezier cb = new CubicBezier(pointsStrip);
        double curvatureRadius = cb.curvatureRadius(0.5);
        System.out.println(curvatureRadius);
        Vector2D center = cb.curvatureCenter(0.5);
        GLUT glut = new GLUT();
        gl.glColor3d(0,0,0);
        gl.glPushMatrix();
        gl.glTranslated(center.getX(), center.getY(), 0);
        glut.glutSolidSphere(curvatureRadius, 50, 50);
        gl.glPopMatrix();

        gl.glColor3d(1,0,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(Vector2D p: pointsStrip.getPoints())
            gl.glVertex2d(p.getX(), p.getY());
        gl.glEnd();

        Vector2D onCurve = cb.value(0.5);
        gl.glColor3d(1,1,0);
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(center.getX(), center.getY());
        gl.glVertex2d(onCurve.getX(), onCurve.getY());
        gl.glEnd();
        gl.glPopMatrix();

        gl.glColor3d(0,1,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        Vector2D p;
        double steps = 30.0;
        for(int i = 0; i < steps; i++) {
            p = cb.value(i/steps);
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();


    }

//    @Override
//    public void mousePressed(MouseEvent e) {
//        int x = e.getX() - getWidth() / 2;
//        int y = getHeight() / 2 - e.getY();
//        pointsStrip.addPoint(new Vector2D(x, y));
//        cnt++;
//        if (cnt == 4) {
//            display();
//            pointsStrip = new PointsStrip();
//            cnt = 0;
//        }
//    }


    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX()-getWidth()/2;
        int y = getHeight()/2-e.getY();
        pointsStrip.addPoint(new Vector2D(x, y));
        display();
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER :
                pointsStrip = new PointsStrip();
                cubics = new ArrayList<>();
                display();
                break;
            case KeyEvent.VK_F :
                pointsStrip = pointsStrip.removeDuplicates();
                Parameterization parameterization = new ChordParameterization(pointsStrip.getPoints());
                PointsStrip ps = new PointsStrip(pointsStrip.getPoints(), parameterization);
                cubics = ps.fit(20, pointsStrip.get(1).subtract(pointsStrip.get(0)).normalize(), pointsStrip.get(pointsStrip.size()-2).subtract(pointsStrip.get(pointsStrip.size()-1)).normalize());
                System.out.println("Points: " + pointsStrip.size());
                System.out.println("Cubics: " + cubics.size());
                System.out.println("Points in cubics: " + ((cubics.size()-1)*3+4));
//                CubicBezier cb = cubics.get(0);
//                System.out.println(cb.getLength());
//                List<Vector2D> vectors = new ArrayList<>();
//                for(int i = 0; i < 4; i++) {
//                    Vector2D point = cb.getPoint(i);
//                    vectors.add(new Vector2D(point.getX(), point.getY()));
//                }
                cbs = new CubicBezierStrip(cubics);
                display();
                break;
            case KeyEvent.VK_S : // Show info
                System.out.println(cubics.size());
                for(CubicBezier cubicBezier: cubics)
                    System.out.println(cubicBezier);
                break;
        }
    }
}