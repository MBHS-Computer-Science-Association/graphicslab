package graphicslab;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FreeformTesting {
	public static void main(String[] args) throws Exception {
		
		final Window testWindow = new RenderingWindow(800, 600, "A window.", false);
		
		
		testWindow.setInitializeRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			
			ShaderProgram shaderProgram = null;
		    try {
				shaderProgram = new ShaderProgram();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		    rwindow.setShaderProgram(shaderProgram);
		    
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
		    
		    try {
				shaderProgram.createUniform("projectionMatrix");
				shaderProgram.createUniform("worldMatrix");
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    glEnable(GL_DEPTH_TEST);
		    
		    // Initialize Meshes		    
		    float[] positions = new float[] {
		    	    // VO
		    	    -0.5f,  0.5f,  0.5f,
		    	    // V1
		    	    -0.5f, -0.5f,  0.5f,
		    	    // V2
		    	    0.5f, -0.5f,  0.5f,
		    	    // V3
		    	     0.5f,  0.5f,  0.5f,
		    	    // V4
		    	    -0.5f,  0.5f, -0.5f,
		    	    // V5
		    	     0.5f,  0.5f, -0.5f,
		    	    // V6
		    	    -0.5f, -0.5f, -0.5f,
		    	    // V7
		    	     0.5f, -0.5f, -0.5f,
		    	};
		    
		    float[] colors = new float[]{
		    	    0.5f, 0.0f, 0.0f,
		    	    0.0f, 0.5f, 0.0f,
		    	    0.0f, 0.0f, 0.5f,
		    	    0.0f, 0.5f, 0.5f,
		    	    0.5f, 0.0f, 0.0f,
		    	    0.0f, 0.5f, 0.0f,
		    	    0.0f, 0.0f, 0.5f,
		    	    0.0f, 0.5f, 0.5f,
		    	};
		    
		    int[] indices = new int[] {
		    	    // Front face
		    	    0, 1, 3, 3, 1, 2,
		    	    // Top Face
		    	    4, 0, 3, 5, 4, 3,
		    	    // Right face
		    	    3, 2, 7, 5, 3, 7,
		    	    // Left face
		    	    0, 1, 6, 4, 0, 6,
		    	    // Bottom face
		    	    6, 1, 2, 7, 6, 2,
		    	    // Back face
		    	    4, 6, 7, 5, 4, 7,
		    	};
		    
		    Mesh mesh = new Mesh(positions, colors, indices);
			
		    Item item = new Item(mesh);
		    item.setPosition(new Vector3f(0.0f, 0.0f, -2.05f));
		    
		    rwindow.transformation = new Transformation();
		    
			rwindow.items = new ArrayList<>();
			rwindow.items.add(item);
		});
		

		testWindow.setStateRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_X)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() - 0.1f);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() + 0.1f);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {				
				rwindow.items.get(0).getRotation().x += 0.1f;
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				System.out.println("space is pressed");
			}
		});
		
		testWindow.setRenderRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			ShaderProgram shaderProgram = window.getShaderProgram();

		    if ( window.isResized() ) {
		        glViewport(0, 0, window.getWidth(), window.getHeight());
		        window.setResized(false);
		    }

			Transformation transformation = rwindow.transformation;

		    // perspective point of view
		    /**
		     * Field of View in Radians
		     */
		    final float FOV = (float) Math.toRadians(60.0f);

		    final float Z_NEAR = 0.01f;

		    final float Z_FAR = 1000.f;

		    Matrix4f projectionMatrix;

		    projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
			shaderProgram.setUniform("projectionMatrix", projectionMatrix);

			for (Item item : rwindow.items) {
		        Matrix4f worldMatrix =
		            transformation.getWorldMatrix(
		                item.getPosition(),
		                item.getRotation(),
		                item.getScale());
				
		        shaderProgram.setUniform("worldMatrix", worldMatrix);
				item.getMesh().render();
			}

		});
		
		testWindow.createWindow();
		testWindow.showWindow();
		
		testWindow.startLoop();
	}
}
