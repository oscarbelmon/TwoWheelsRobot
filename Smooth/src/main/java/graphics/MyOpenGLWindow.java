package graphics;

import algorithm.DouglassPeucker;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import geometry.CubicBezier;
import geometry.Point;
import geometry.PointsStrip;
import geometry.Vector;
import graphics.myopengl.OpenGLWindow;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/2/16.
 */
public class MyOpenGLWindow extends OpenGLWindow {
//    private List<Point> points = new ArrayList<>();
    private PointsStrip points = new PointsStrip();
    private int cnt = 0;

    public MyOpenGLWindow(String title) {
        super(title);
    }

    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
//        if(cnt == 4) bezier(gl);
        renderAllPoints(gl);
        renderFilteredPoints(gl);
        renderTangents(gl);
        gl.glPopMatrix();
    }

    private void renderTangents(GL2 gl) {
        if(points.size() < 2) return;
        Vector origin = points.getTangentNormalizedAtStart().scale(20);
        Point destination = points.get(0).sum(origin);
        gl.glColor3d(0, 1, 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2d(points.get(0).getX(), points.get(0).getY());
        gl.glVertex2d(destination.getX(), destination.getY());
        gl.glEnd();
    }


    private void renderFilteredPoints(GL2 gl) {
        PointsStrip filtered = new DouglassPeucker(points).simplify(5);
        gl.glColor3d(1, 0, 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for(Point point: filtered.getPoints())
            gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    private void renderAllPoints(GL2 gl) {
        gl.glColor3d(0, 0, 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for(Point point: points.getPoints())
            gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
    }

    @Override
    public void lights(GL2 gl) {
    }

    @Override
    public void init(GL2 gl) {
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glEnable(GL2.GL_DOUBLEBUFFER);
    }

    private void bezier(GL2 gl) {
        CubicBezier cb = new CubicBezier(points);
        double curvatureRadius = cb.curvatureRadius(0.5);
        Point center = cb.curvatureCenter(0.5);
        GLUT glut = new GLUT();
        gl.glColor3d(0,0,0);
        gl.glPushMatrix();
        gl.glTranslated(center.getX(), center.getY(), 0);
        glut.glutSolidSphere(curvatureRadius, 50, 50);
        gl.glPopMatrix();

        gl.glColor3d(1,0,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(Point p: points.getPoints())
            gl.glVertex2d(p.getX(), p.getY());
        gl.glEnd();

        Point onCurve = cb.value(0.5);
        gl.glColor3d(1,1,0);
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(center.getX(), center.getY());
        gl.glVertex2d(onCurve.getX(), onCurve.getY());
        gl.glEnd();
        gl.glPopMatrix();

        gl.glColor3d(0,1,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        Point p;
        double steps = 30.0;
        for(int i = 0; i < steps; i++) {
            p = cb.value(i/steps);
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();


    }

//    @Override
//    public void mousePressed(MouseEvent e) {
//        int x = e.getX()-getWidth()/2;
//        int y = getHeight()/2-e.getY();
//        points.add(new Point(x, y));
//        cnt++;
//        if(cnt == 4) {
//            display();
//            points = new ArrayList<>();
//            cnt = 0;
//        }
//    }


    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX()-getWidth()/2;
        int y = getHeight()/2-e.getY();
        points.addPoint(new Point(x, y));
        display();
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
            points = new PointsStrip();
    }
}