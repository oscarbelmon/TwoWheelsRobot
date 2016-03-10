package graphics.myopengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

public class MyGLEventListener implements GLEventListener {
    private OpenGLWindow window;
	
	public MyGLEventListener(OpenGLWindow window) {
		this.window = window;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glPushMatrix();
		window.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		window.init(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int xPos, int yPos, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
	}
}
