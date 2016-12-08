package graphicslab;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FreeformTesting {
	public static void main(String[] args) throws Exception {
		final Window testWindow = new RenderingWindow(800, 600, "A window.");
		
	    float[] positions = new float[] {
	            // V0
	            -0.5f, 0.5f, 0.5f,
	            // V1
	            -0.5f, -0.5f, 0.5f,
	            // V2
	            0.5f, -0.5f, 0.5f,
	            // V3
	            0.5f, 0.5f, 0.5f,
	            // V4
	            -0.5f, 0.5f, -0.5f,
	            // V5
	            0.5f, 0.5f, -0.5f,
	            // V6
	            -0.5f, -0.5f, -0.5f,
	            // V7
	            0.5f, -0.5f, -0.5f,
	            
	            // For text coords in top face
	            // V8: V4 repeated
	            -0.5f, 0.5f, -0.5f,
	            // V9: V5 repeated
	            0.5f, 0.5f, -0.5f,
	            // V10: V0 repeated
	            -0.5f, 0.5f, 0.5f,
	            // V11: V3 repeated
	            0.5f, 0.5f, 0.5f,

	            // For text coords in right face
	            // V12: V3 repeated
	            0.5f, 0.5f, 0.5f,
	            // V13: V2 repeated
	            0.5f, -0.5f, 0.5f,

	            // For text coords in left face
	            // V14: V0 repeated
	            -0.5f, 0.5f, 0.5f,
	            // V15: V1 repeated
	            -0.5f, -0.5f, 0.5f,

	            // For text coords in bottom face
	            // V16: V6 repeated
	            -0.5f, -0.5f, -0.5f,
	            // V17: V7 repeated
	            0.5f, -0.5f, -0.5f,
	            // V18: V1 repeated
	            -0.5f, -0.5f, 0.5f,
	            // V19: V2 repeated
	            0.5f, -0.5f, 0.5f,
	        };
	        float[] textureCoords = new float[]{
	            0.0f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.5f, 0.0f,
	            
	            0.0f, 0.0f,
	            0.5f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            
	            // For text coords in top face
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.0f, 1.0f,
	            0.5f, 1.0f,

	            // For text coords in right face
	            0.0f, 0.0f,
	            0.0f, 0.5f,

	            // For text coords in left face
	            0.5f, 0.0f,
	            0.5f, 0.5f,

	            // For text coords in bottom face
	            0.5f, 0.0f,
	            1.0f, 0.0f,
	            0.5f, 0.5f,
	            1.0f, 0.5f,
	        };
	        int[] indices = new int[]{
	            // Front face
	            0, 1, 3, 3, 1, 2,
	            // Top Face
	            8, 10, 11, 9, 8, 11,
	            // Right face
	            12, 13, 7, 5, 12, 7,
	            // Left face
	            14, 15, 6, 4, 14, 6,
	            // Bottom face
	            16, 18, 19, 17, 16, 19,
	            // Back face
	            4, 6, 7, 5, 4, 7,};
	        
	    Mesh mesh = new Mesh(positions, textureCoords, indices);
	    mesh.setTexture(new Texture("src/test/cute.png"));
	    Mesh skyboxMesh = new Mesh(positions, textureCoords, indices);
	    skyboxMesh.setTexture(new Texture("src/test/th.jpg"));
		
	    RenderingWindow rw = (RenderingWindow) testWindow;
	    
	    Item skybox = new Item(skyboxMesh);
	    skybox.setPosition(new Vector3f(0.0f, 0.0f, 0f));
	    skybox.setScale(1000f);
	    rw.items.add(skybox);
	    
	    
		
		
		
		testWindow.setInitializeRoutine((window) -> {
			ShaderProgram shaderProgram = window.getRenderer().getShaderProgram();
		    
		    try {
				shaderProgram.createUniform("projectionMatrix");
				shaderProgram.createUniform("modelViewMatrix");
				shaderProgram.createUniform("texture_sampler");
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		});
		
		testWindow.setInputRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;

			final float MOUSE_SENSITIVITY = 1.0f;
			
//		    if (rwindow.mouse.isLeftButtonPressed()) {
		        Vector2f rotVec = rwindow.mouse.getDisplVec();
		        rwindow.camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//		    }
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_U)) {
				rwindow.camera.movePosition(0, 0, -1.0f);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_J)) {
				rwindow.camera.movePosition(0, 0, 1.0f);
			}

			if (window.isKeyPressed(GLFW.GLFW_KEY_F)) {
				rwindow.camera.movePosition(-1.0f, 0, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_H)) {
				rwindow.camera.movePosition(1.0f, 0, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_T)) {
				rwindow.camera.movePosition(0, 1.0f, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_G)) {
				rwindow.camera.movePosition(0, -1.0f, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
				rwindow.camera.moveRotation(1.0f, 0, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
				rwindow.camera.moveRotation(-1.0f, 0, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
				rwindow.camera.moveRotation(0, -1.0f, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
				rwindow.camera.moveRotation(0, 1.0f, 0);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_K)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() - 10f);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_L)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() + 10f);
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {				
				rwindow.items.get(0).getRotation().x += 1f;
			}
			if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {				
				rwindow.items.get(0).getRotation().x += -1f;
			}

			if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {				
				rwindow.items.get(0).getRotation().y += 1f;
			}
			if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {				
				rwindow.items.get(0).getRotation().y += -1f;
			}

			if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {				
				rwindow.items.get(0).getRotation().z += 1f;
			}
			if (window.isKeyPressed(GLFW.GLFW_KEY_E)) {				
				rwindow.items.get(0).getRotation().z += -1f;
			}
			
			if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				System.out.println("space is pressed");
			}
		});
		
		Random r = new Random();
		
		testWindow.setStateRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			Item item = new Item(mesh);
			
			float x = r.nextFloat() * 1000 - 500;
			float y = 550;
			float z = r.nextFloat() * 1000 - 500;
			item.setPosition(new Vector3f(x, y, z));

			x = r.nextFloat() * 360;
			y = r.nextFloat() * 360;
			z = r.nextFloat() * 360;
			item.setRotation(new Vector3f(x, y, z));
			
			rwindow.items.add(item);
			
			for (Item i : rwindow.items) {
				if (i.getPosition().equals(new Vector3f())) {
					continue;
				}
				
				Vector3f position = i.getPosition();
				i.setPosition(position.add(0, -1f, 0));

				if (position.x > 0) {
					i.setPosition(position.add(-0.5f, 0, 0));
				} else if (position.x < -1){
					i.setPosition(position.add(0.5f, 0, 0));
				}
				
				if (position.z > 0) {
					i.setPosition(position.add(0, 0, -0.5f));
				} else if (position.z < -1){
					i.setPosition(position.add(0, 0, 0.5f));
				}
			}
		});
		
		
		testWindow.setRenderRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			ShaderProgram shaderProgram = window.getRenderer().getShaderProgram();
			Camera camera = rwindow.camera;
			Transformation transformation = rwindow.transformation;

		    /**
		     * Field of View in Radians
		     */
		    final float FOV = (float) Math.toRadians(60.0f);
		    final float Z_NEAR = 0.01f;
		    final float Z_FAR = 1000.f;

		    Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
			shaderProgram.setUniform("projectionMatrix", projectionMatrix);

			shaderProgram.setUniform("texture_sampler", 0);
			
			Matrix4f viewMatrix = transformation.getViewMatrix(camera);
			
			for (Item item : rwindow.items) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				
		        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
		        if (item.getPosition().equals(new Vector3f())) {
		        	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		        } else {
		        	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		        }
				item.getMesh().render();
			}

		});
		
		testWindow.createWindow();
		
		testWindow.startLoop();
	}
}
