package graphics.myopengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class OpenGLWindow extends ComponentAdapter implements MouseListener, MouseMotionListener { //, KeyListener {
//	private JFrame jpPanel;
	private JPanel jpPanel;
    private JFrame jfWindow;
	private GLCanvas glCanvas;
	private MyGLEventListener glListener;
    private JPanel infoPanel;

	public OpenGLWindow() {
		createGUI("OpenGL Window");
	}
	
	public OpenGLWindow(String title) {
		createGUI(title);
	}
	
	public void componentResized(ComponentEvent e) {
		display();
	}

	private void createGUI(String title) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        glCanvas = new GLCanvas(caps);
        jpPanel = new JPanel();
        jpPanel.setLayout(new BorderLayout());
		jfWindow = new JFrame(title);
		glCanvas.addComponentListener(this);
		glCanvas.addMouseListener(this);
        glCanvas.addMouseMotionListener(this);
//        glCanvas.addKeyListener(this);
		glListener = new MyGLEventListener(this);
		glCanvas.addGLEventListener(glListener);
        glCanvas.setSize(400, 480);
        jpPanel.add(glCanvas);
		jfWindow.getContentPane().add(jpPanel);
        jfWindow.getContentPane().add(infoPanel(), BorderLayout.EAST);
        jfWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jfWindow.pack();
        jfWindow.setVisible(true);
	}

    protected abstract JPanel infoPanel();

    public int getWidth() {
        return glCanvas.getWidth();
    }

    public int getHeight() {
        return glCanvas.getHeight();
    }
	
	public void display() {
		glCanvas.display();
	}
	
	public abstract void render(GL2 gl);
	public abstract void lights(GL2 gl);
	public abstract void init(GL2 gl);

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

//    @Override
//    public void keyTyped(KeyEvent e) {
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//    }

}
