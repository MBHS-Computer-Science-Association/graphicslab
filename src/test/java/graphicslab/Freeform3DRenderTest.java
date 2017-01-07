package graphicslab;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import graphicslab.assets.Cube;
import graphicslab.lighting.Material;
import graphicslab.lighting.PointLight;
import graphicslab.lighting.PointLight.Attenuation;
import graphicslab.window.Window;
import graphicslab.window.input.KeyboardInput;
import graphicslab.window.input.MouseInput;

public class Freeform3DRenderTest {

	public static void main(String[] args) throws Exception {
	    final Random random = new Random();
		final Window testWindow = new Window(800, 600, "A window.");
	    final Renderer3D renderer = new Renderer3D();
		final KeyboardInput keyboard = new KeyboardInput();
	    final MouseInput mouse = new MouseInput();
		
	    final Scene3D scene = new Scene3D();

	    final Mesh ballMesh = OBJLoader.loadMesh("src/test/ball.obj");
	    ballMesh.setMaterial(new Material(new Texture("src/test/skybox.png")));
	    
	    final List<Item> items = scene.getItems();

	    
	    testWindow.addInputHandler(keyboard);
	    testWindow.addInputHandler(mouse);
	    
		testWindow.setInitializeRoutine((window) -> {
			renderer.init();
			
			Skybox skybox = new Skybox("src/test/skybox.obj", "src/test/skybox.png");
			skybox.setScale(750);
			scene.setSkybox(skybox);
		});
		
		testWindow.setInputRoutine((window) -> {
		    Camera camera = renderer.getCamera();
			
			final float MOUSE_SENSITIVITY = 1.0f;
			
	        Vector2f rotVec = mouse.getDisplVec();
	        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				camera.movePosition(0, 1.0f, 0);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
				camera.movePosition(0, -1.0f, 0);
			}

			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
				camera.movePosition(-1.0f, 0, 0);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
				camera.movePosition(1.0f, 0, 0);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_W)) {
				camera.movePosition(0, 0, -1.0f);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_S)) {
				camera.movePosition(0, 0, 1.0f);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_M)) {
			    window.setCursorInputMode(GLFW_CURSOR_NORMAL);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_N)) {
				window.setCursorInputMode(GLFW_CURSOR_DISABLED);
			}

            if (keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                window.setWindowShouldClose(true);
            }
			
		});
		
		
		testWindow.setStateRoutine((window) -> {
		    
//		    if (keyboard.isKeyPressed('B')) {		        
		        float x, y, z;
		        
		        Item item = new Cube();
//		        Item item = new Item(ballMesh);
		        item.setScale(5f);
		        
		        if (items.size() > 10000) {
		            items.remove(0);
		        }
		        
		        x = random.nextFloat() * 360;
		        y = random.nextFloat() * 360;
		        z = random.nextFloat() * 360;
		        item.setRotation(new Vector3f(x, y, z));
		        
		        x = random.nextFloat() * 200 - 100;
		        y = random.nextFloat() * 200 - 100;
		        z = random.nextFloat() * 200 - 100;
		        
		        Vector3f direction = new Vector3f().sub(new Vector3f(x, y, z)).normalize();
		        item.setPosition(direction.mul(100 + 5 * random.nextFloat()));
		        items.add(item);
//		    }

			 spinCubes(items);


			
//			items.removeIf((i) -> {
//			    float distance = i.getPosition().distance(new Vector3f());
//			    return distance < 5 || distance > 5000;
//			});
			
			List<PointLight> pointLights = scene.getSceneLighting().getPointLights();
			
			pointLights.clear();

	        PointLight pointLight = new PointLight(new Vector3f(0.0f, 1.0f, 1.0f), new Vector3f(0, -500, 0), 50.0f);
	        Attenuation att = pointLight.new Attenuation(0.01f, 0.001f, 0.001f);
	        pointLight.setAttenuation(att);
	        pointLights.add(pointLight);
	       
	        PointLight pointLight2 = new PointLight(new Vector3f(1, 1, 1), new Vector3f(0, 0, 500), 50.0f);
	        pointLight2.setAttenuation(att);
	        pointLights.add(pointLight2);
	        
	        PointLight pointLight3 = new PointLight(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(500, 0, 0), 50.0f);
	        pointLight3.setAttenuation(att);
	        pointLights.add(pointLight3);

	        PointLight pointLight4 = new PointLight(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0, 500, 0), 50.0f);
	        pointLight4.setAttenuation(att);
	        pointLights.add(pointLight4);
	        
            PointLight pointLight5 = new PointLight(new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-500, 0, 0), 50.0f);
            pointLight5.setAttenuation(att);
            pointLights.add(pointLight5);
		});
		
		
		testWindow.setRenderRoutine((window) -> {
		    scene.setDimensions(window.getWidth(), window.getHeight());
		    renderer.render(scene);
		});
		
		testWindow.createWindow();
		testWindow.showWindow();
		testWindow.startLoop();
	}
	
    public static void spinCubes(List<Item> items) {

        float x, y, z;
        Random random = new Random();
        
        for (Item i : items) {

            if (i instanceof Cube) {

                Vector3f rot = i.getRotation();

                x = random.nextFloat() * 0.5f;
                y = random.nextFloat() * 0.5f;
                z = random.nextFloat() * 0.5f;

                i.setRotation(new Vector3f(rot.x + x, rot.y + y, rot.z + z));

                Vector3f origin = new Vector3f();

                Vector3f direction = i.getPosition().normalize();
                Vector3f position = i.getPosition();

                Vector3f velocity = new Vector3f();

                float distance = direction.distance(origin) - 300;
                velocity.set(direction.normalize().mul(-distance));
                velocity.mul(0.0000001f);
                position.add(velocity);
            }
        }
	}
}
