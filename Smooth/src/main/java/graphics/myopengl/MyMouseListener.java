package graphics.myopengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyMouseListener implements MouseListener, MouseMotionListener {
    private OpenGLWindow window;
    
    public MyMouseListener(OpenGLWindow window) {
    	this.window = window;
    }
    
	public void mousePressed(MouseEvent e) {
		System.out.println(e.getX() + ", " + e.getY());
	}
	
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent arg0) {
	}
}
