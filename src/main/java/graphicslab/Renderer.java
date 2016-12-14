package graphicslab;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL;

import graphicslab.window.RenderRoutine;
import graphicslab.window.Window;

public class Renderer {
	private ShaderProgram shaderProgram;

	public Renderer() {
		
	}
	
	public void init() {
		
		
		GL.createCapabilities();
		
	    try {
			shaderProgram = new ShaderProgram();
			shaderProgram.createVertexShader(Utils.loadResource("src/main/res/vertex.vs"));
			shaderProgram.createFragmentShader(Utils.loadResource("src/main/res/fragment.fs"));
			shaderProgram.link();
		} catch (Exception e) {
			e.printStackTrace();
		}

	    glEnable(GL_DEPTH_TEST);
	}
	
	public void render(Window window, RenderRoutine renderRoutine) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		shaderProgram.bind();
		
	    if (window.isResized()) {
	        glViewport(0, 0, window.getWidth(), window.getHeight());
	        window.setResized(false);
	    }
		
		if (renderRoutine != null) {			
			renderRoutine.loop(window);
		}
		
		shaderProgram.unbind();

		glfwSwapBuffers(window.getPointer()); // swap the color buffers
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
}
