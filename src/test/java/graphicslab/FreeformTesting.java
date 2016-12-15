package graphicslab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import static org.lwjgl.glfw.GLFW.*;

import graphicslab.audio.SoundBuffer;
import graphicslab.audio.Source;
import graphicslab.lighting.Material;
import graphicslab.lighting.PointLight;
import graphicslab.lighting.PointLight.Attenuation;
import graphicslab.window.RenderingWindow;
import graphicslab.window.Window;

public class FreeformTesting {
	static SoundBuffer buffBack = null;
	static {
		try {
			buffBack = new SoundBuffer("src/test/tone.ogg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		final Window testWindow = new RenderingWindow(800, 600, "A window.");
	    
	    Mesh cube = OBJLoader.loadMesh("src/test/cube.obj");
	    Texture grass = new Texture("src/test/grassblock.png");
	    Material grassBlockMaterial = new Material(grass, 1f);
	    cube.setMaterial(grassBlockMaterial);
	    
	    Mesh skybox = OBJLoader.loadMesh("src/test/cube.obj");
	    Texture sky = new Texture("src/test/sky.jpg");
	    Material skyMaterial = new Material(sky, 1f);
	    skybox.setMaterial(skyMaterial);
	    
	    Mesh bunnyMesh = OBJLoader.loadMesh("src/test/bunny.obj");
	    Material bunnyMaterial = new Material(new Vector3f(0.5f, 0.5f, 0.7f), 1f);
	    bunnyMesh.setMaterial(bunnyMaterial);
	    
	    RenderingWindow rw = (RenderingWindow) testWindow;
	    
//	    Item bunnyItem = new Item(bunnyMesh);
//	    bunnyItem.setScale(100f);
//	    bunnyItem.setPosition(new Vector3f(0, -50f, -300f));
//	    rw.items.add(bunnyItem);
	    
	    Item skyboxItem = new Item(skybox);
	    skyboxItem.setScale(500f);
	    rw.items.add(skyboxItem);
	    
		
		testWindow.setInitializeRoutine((window) -> {
			ShaderProgram shaderProgram = window.getRenderer().getShaderProgram();
		    
		    try {
				shaderProgram.createUniform("projectionMatrix");
				shaderProgram.createUniform("modelViewMatrix");
				shaderProgram.createUniform("texture_sampler");
				shaderProgram.createMaterialUniform("material");
				
				shaderProgram.createUniform("specularPower");
				shaderProgram.createUniform("ambientLight");
				shaderProgram.createPointLightListUniform("pointLights", 4);
				
		        shaderProgram.createUniform("debug");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		testWindow.setInputRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;

			
			final float MOUSE_SENSITIVITY = 1.0f;
			
	        Vector2f rotVec = rwindow.mouse.getDisplVec();
	        rwindow.camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
			
	        // Crazy camera!
	        if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_P)) {
	        	rwindow.camera.moveRotation(1.0f, 0, 0);
	        	rwindow.camera.moveRotation(0, -1.0f, 0);
	        }
	        
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				rwindow.camera.movePosition(0, 1.0f, 0);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
				rwindow.camera.movePosition(0, -1.0f, 0);
			}

			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
				rwindow.camera.movePosition(-1.0f, 0, 0);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
				rwindow.camera.movePosition(1.0f, 0, 0);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_W)) {
				rwindow.camera.movePosition(0, 0, -1.0f);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_S)) {
				rwindow.camera.movePosition(0, 0, 1.0f);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_M)) {
				glfwSetInputMode(window.getPointer(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_N)) {
				glfwSetInputMode(window.getPointer(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_K)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() - 1f);
			}
			
			if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_L)) {				
				rwindow.items.get(0).setScale(rwindow.items.get(0).getScale() + 1f);
			}
		});
		
		Random r = new Random();
		
		testWindow.setStateRoutine((window) -> {
			RenderingWindow rwindow = (RenderingWindow) window;
			
			rw.soundManager.updateListenerPosition(rw.camera);
			
			float x, y, z;
			
			if (rw.keyboard.isKeyPressed(GLFW_KEY_B)) {
				SoundingItem item = new SoundingItem(cube);
				
				Source source = item.getSoundSource();
				source.init();
				source.setBuffer(buffBack.getId());
				source.play();
				
				item.setScale(5f);
				
				x = r.nextFloat() * 1000 - 500;
				y = 500;
				z = r.nextFloat() * 1000 - 500;
				item.setPosition(new Vector3f(x, y, z));
	
				x = r.nextFloat() * 360;
				y = r.nextFloat() * 360;
				z = r.nextFloat() * 360;
				item.setRotation(new Vector3f(x, y, z));
				rwindow.items.add(item);
			}
			
			Set<Item> removeSet = new HashSet<>();
			
			for (Item i : rwindow.items) {
				if (i instanceof SoundingItem) {
					SoundingItem sitem = (SoundingItem) i;
					
					Vector3f rot = i.getRotation();
					
					x = r.nextFloat() * 0.5f;
					y = r.nextFloat() * 0.5f;
					z = r.nextFloat() * 0.5f;
					
					i.setRotation(new Vector3f(rot.x + x, rot.y + y, rot.z + z));

					if (i.getPosition().y < -800) {
						removeSet.add(i);
					}
					Vector3f direction = new Vector3f(0, -500, 0).sub(i.getPosition());
					Vector3f position = i.getPosition();
					
					float factor = (600f - position.y) / 50f;
					sitem.setVelocity(direction.normalize().mul(factor, 0, factor));
					float factor2 = r.nextFloat();
					sitem.getVelocity().add(0, -0.5f * factor + factor2, 0);
					sitem.update();
				}
			}
			
			for (Item i : removeSet) {
				if (i instanceof SoundingItem) {
					SoundingItem sitem = (SoundingItem) i;
					sitem.getSoundSource().stop();
					sitem.getSoundSource().cleanup();
				}
				rw.items.remove(i);
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
		    final float Z_FAR = 2000.f;

		    Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
			
		    shaderProgram.setUniform("projectionMatrix", projectionMatrix);
			shaderProgram.setUniform("texture_sampler", 0);

			Vector3f ambientLight = new Vector3f(0.01f, 0.01f, 0.01f);
			float specularPower = 3f;
	        shaderProgram.setUniform("ambientLight", ambientLight);
	        shaderProgram.setUniform("specularPower", specularPower);
	        
	        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
	        
	        List<PointLight> pointLights = new ArrayList<>();
	        
	        PointLight pointLight = new PointLight(new Vector3f(0.0f, 1.0f, 1.0f), new Vector3f(0, -200, 0), 50.0f);
	        Attenuation att = pointLight.new Attenuation(0.01f, 0.001f, 0.001f);
	        pointLight.setAttenuation(att);
	        pointLights.add(pointLight);
	        
	        float x = r.nextFloat() * 1000 - 500;
	        float y = r.nextFloat() * 1000 - 500;
	        float z = r.nextFloat() * 1000 - 500;

	        float red = r.nextFloat();
	        float green = r.nextFloat();
	        float blue = r.nextFloat();
	        
	        PointLight pointLight2 = new PointLight(new Vector3f(red, green, blue), new Vector3f(x, y, z), 100.f);
	        pointLight2.setAttenuation(att);
	        pointLights.add(pointLight2);
	        
	        PointLight pointLight3 = new PointLight(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(camera.getPosition()), 50.0f);
	        pointLight3.setAttenuation(att);
	        pointLights.add(pointLight3);

	        PointLight pointLight4 = new PointLight(new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(0, 400, 0), 50.0f);
	        pointLight4.setAttenuation(att);
	        pointLights.add(pointLight4);
	        
	        for (int i = 0; i < pointLights.size(); i++) {
	        	PointLight currPointLight = new PointLight(pointLights.get(i));
	        	Vector3f lightPos = currPointLight.getPosition();
	        	Vector4f aux = new Vector4f(lightPos, 1);
	        	aux.mul(viewMatrix);
	        	lightPos.x = aux.x;
	        	lightPos.y = aux.y;
	        	lightPos.z = aux.z;
	        	shaderProgram.setUniform("pointLights", currPointLight, i);	      
	        }
	        

	        shaderProgram.setUniform("debug", getWindowDebugState(window));
			
			for (Item item : rwindow.items) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				
		        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
		        shaderProgram.setUniform("material", item.getMesh().getMaterial());
		        
				item.getMesh().render();
			}
		});
		
		testWindow.createWindow();
		testWindow.showWindow();
		testWindow.startLoop();
		
		rw.soundManager.cleanup();
		
	}
	
	public static int getWindowDebugState(Window window) {
		RenderingWindow rw = (RenderingWindow) window;
		
		int value = 0;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_1)) value = 1;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_2)) value = 2;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_3)) value = 3;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_4)) value = 4;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_5)) value = 5;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_6)) value = 6;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_7)) value = 7;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_8)) value = 8;
		if (rw.keyboard.isKeyPressed(GLFW.GLFW_KEY_9)) value = 9;
		return value;
	}
	
}
