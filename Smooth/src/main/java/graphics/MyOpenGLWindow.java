package graphics;

import com.jogamp.opengl.GL2;
import geometry.CubicBezier;
import geometry.Point;
import graphics.myopengl.OpenGLWindow;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/2/16.
 */
public class MyOpenGLWindow extends OpenGLWindow {
    private List<Point> points = new ArrayList<>();
    private int cnt = 0;

    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3d(0,0,0);
        if(cnt == 4) bezier(gl);
        gl.glPopMatrix();
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
        System.out.println("Dibuja");

        gl.glColor3d(1,0,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(Point p: points)
            gl.glVertex2d(p.getX(), p.getY());
        gl.glEnd();

        CubicBezier cb = new CubicBezier(points);
        gl.glColor3d(0,0,0);
        gl.glBegin(GL2.GL_LINE_STRIP);
        Point p;
        double steps = 20.0;
        for(int i = 0; i < steps; i++) {
            p = cb.value(i/steps);
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        int x = e.getX()-getWidth()/2;
        int y = getHeight()/2-e.getY();
        points.add(new Point(x, y));
        cnt++;
        if(cnt == 4) {
            display();
            points = new ArrayList<>();
            cnt = 0;
        }
    }
}