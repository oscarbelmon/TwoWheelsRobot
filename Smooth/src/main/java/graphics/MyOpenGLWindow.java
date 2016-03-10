package graphics;

import algorithm.ChordParameterization;
import algorithm.DouglassPeucker;
import algorithm.NewtonRaphsonParameterization;
import algorithm.Parameterization;
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
    private PointsStrip points = new PointsStrip();
    private int cnt = 0;
    private List<CubicBezier> cb = new ArrayList<>();

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
//        if(cb != null)
        if(!cb.isEmpty()) {
            gl.glColor3d(1, 0, 0);
            for (CubicBezier cubic : cb)
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

    private void coso2(GL2 gl) {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Point(-100, 100));
        ps.addPoint(new Point(100, 100));
        ps.addPoint(new Point(100, -100));
        ps.addPoint(new Point(-100, -100));
        CubicBezier cb = new CubicBezier(ps);
        gl.glColor3d(0,1,0);
        bezier2(gl, cb);

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            points.add(cb.value(i/20.0));
        }

        Parameterization parameterization = new ChordParameterization(points);
        PointsStrip ps2 = new PointsStrip(points, parameterization);
        List<CubicBezier> cubics = ps2.fit(10);
        gl.glColor3d(1,0,0);
        for(CubicBezier cubic: cubics)
            bezier2(gl, cubic);
    }

    private void coso(GL2 gl) {
        PointsStrip ps = new PointsStrip();
        ps.addPoint(new Point(-100, 100));
        ps.addPoint(new Point(100, 100));
        ps.addPoint(new Point(100, -100));
        ps.addPoint(new Point(-100, -100));
        CubicBezier cb = new CubicBezier(ps);
        gl.glColor3d(0,1,0);
        bezier2(gl, cb);

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            points.add(cb.value((double)i/20.0));
        }
        Parameterization parameterization = new ChordParameterization(points);
        PointsStrip ps2 = new PointsStrip(points, parameterization);
        CubicBezier cb2 = ps2.fit();
        gl.glColor3d(1,0,0);
        bezier2(gl, cb2);

        PointsStrip ps3 = new PointsStrip(points, new NewtonRaphsonParameterization(parameterization, cb2));
        CubicBezier cb3 = ps3.fit();
        gl.glColor3d(0,0,1);
        bezier2(gl, cb3);

    }

    private void bezier2(GL2 gl, CubicBezier cb) {
        gl.glBegin(GL2.GL_LINE_STRIP);
        Point p;
        double steps = 30.0;
        for(int i = 0; i <= steps; i++) {
            p = cb.value(i/steps);
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();

        // Control points
//        gl.glColor3d(0,0,0);
//        gl.glBegin(GL.GL_LINE_STRIP);
//        for(int i = 0; i < 4; i++)
//            gl.glVertex2d(cb.getPoint(i).getX(), cb.getPoint(i).getY());
//        gl.glEnd();
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
//        gl.glColor3d(0, 0, 0);
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
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER :
                points = new PointsStrip();
                cb = new ArrayList<>();
                break;
            case KeyEvent.VK_F :
                Parameterization parameterization = new ChordParameterization(points.getPoints());
                PointsStrip ps = new PointsStrip(points.getPoints(), parameterization);
                cb = ps.fit(20);
                System.out.println("Points: " + points.size());
                System.out.println("Cubics: " + cb.size());
                System.out.println("Points in cubics: " + ((cb.size()-1)*3+4));
//                parameterization = new NewtonRaphsonParameterization(parameterization, cb);
////                System.out.println(cb);
//                cb = new PointsStrip(points.getPoints(), parameterization).fit();
                display();
                break;
        }
    }
}