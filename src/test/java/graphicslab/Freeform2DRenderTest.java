package graphicslab;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import graphicslab.assets.Cube;
import graphicslab.window.Window;
import graphicslab.window.input.KeyboardInput;

public class Freeform2DRenderTest {

	public static void main(String[] args) throws Exception {
	    final Random random = new Random();
		final Window testWindow = new Window(800, 600, "A window.");
	    final Renderer2D renderer = new Renderer2D();
		final KeyboardInput keyboard = new KeyboardInput();
		final Transformation transformation = new Transformation();
	    
		final List<Item> items = new LinkedList<>();
		final Camera camera = new Camera(new Vector3f(), new Vector3f());
		
		items.add(new TextItem("Hello", "src/test/font_texture.png", 16, 16));
		
		testWindow.addInputHandler(keyboard);
	    
		testWindow.setInitializeRoutine((window) -> {
			renderer.init();
		});
		
		testWindow.setInputRoutine((window) -> {
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
			float x, y, z;
			
			if (items.size() > 100) {
			    items.remove(1);
			}
			
		    Cube item = new Cube();
			
			item.setScale(10f);
			
			

			x = random.nextInt() % 8;
			y = random.nextInt() % 8;
			z = 0;
//			z = r.nextInt() % 8;
//			item.setRotation(new Vector3f(x * 45, y * 45, z * 45));
            item.setRotation(new Vector3f(45, 45, 0));

            
            x = random.nextInt() % (window.getWidth() / 35) * 35;
            y = random.nextInt() % (window.getHeight() / 35) * 35;
            
            item.setPosition(new Vector3f(x, y, -y / 10000));
            items.add(item);
			
			items.removeIf((i) -> {
			    return i.getPosition().x > window.getWidth() || i.getPosition().x < 0 || i.getPosition().y > window.getHeight() || i.getPosition().y < 0;
			});
		});
		
		
		testWindow.setRenderRoutine((window) -> {
			renderer.begin(window);
				        
			for (Item item : items) {
			    renderer.render(item);
			}
            
			renderer.end();
		});
		
		testWindow.createWindow();
		testWindow.showWindow();
		testWindow.startLoop();
	}
}
