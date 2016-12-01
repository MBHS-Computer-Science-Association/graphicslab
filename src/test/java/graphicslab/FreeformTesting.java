package graphicslab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;


public class FreeformTesting {
	public static void main(String[] args) throws Exception {
		
		StateRoutine state = (window) -> {
			if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				System.out.println("space is pressed");
			}
		};
		
		/////////
		
		InitializeRoutine init = (window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			
			ShaderProgram shaderProgram = null;
		    try {
				shaderProgram = new ShaderProgram();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		    try {
				shaderProgram.createVertexShader(Utils.loadResource("src/main/res/vertex.vs"));
				shaderProgram.createFragmentShader(Utils.loadResource("src/main/res/fragment.fs"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		    try {
				shaderProgram.link();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			float[] vertices = new float[]{
				     0.0f,  0.5f, 0.0f,
				    -0.5f, -0.5f, 0.0f,
				     0.5f, -0.5f, 0.0f
				};
			
			FloatBuffer verticesBuffer =
				    BufferUtils.createFloatBuffer(vertices.length);
				verticesBuffer.put(vertices).flip();
			
			rwindow.vaoId = glGenVertexArrays();
			glBindVertexArray(rwindow.vaoId);
			
			rwindow.vboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, rwindow.vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			
			
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
			shaderProgram.unbind();
		};
		
		RenderRoutine render = (window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			
//			clear();

//		    if ( window.isResized() ) {
//		        glViewport(0, 0, window.getWidth(), window.getHeight());
//		        window.setResized(false);
//		    }
			
			ShaderProgram shaderProgram = window.getShaderProgram();

		    shaderProgram.bind();

		    // Bind to the VAO
		    glBindVertexArray(rwindow.vaoId);
		    glEnableVertexAttribArray(0);

		    // Draw the vertices
		    glDrawArrays(GL_TRIANGLES, 0, 3);

		    // Restore state
		    glDisableVertexAttribArray(0);
		    glBindVertexArray(0);

		    shaderProgram.unbind();
		};
		
		final Window window = new RenderingWindow(800, 600, "My first window.", false);
		
		window.setInitializeRoutine(init);
		window.setStateRoutine(state);
		
		window.createWindow();
		window.showWindow();
		
		window.startLoop();
	}
}
