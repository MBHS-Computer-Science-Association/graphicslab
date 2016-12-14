package graphicslab.window;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import graphicslab.Camera;
import graphicslab.Item;
import graphicslab.ShaderProgram;
import graphicslab.Transformation;
import graphicslab.lighting.PointLight;
import graphicslab.lighting.PointLight.Attenuation;
import graphicslab.sound.AudioManager;
import graphicslab.window.Window;
import graphicslab.window.input.KeyboardInput;
import graphicslab.window.input.MouseInput;

public class RenderingWindow extends Window{
	public List<Item> items;
	public KeyboardInput keyboard;
	public Transformation transformation;
	public Camera camera;
	public MouseInput mouse;
	public List<PointLight> pointLights;
	public AudioManager soundManager;

	
	
	public RenderingWindow(int width, int height, String title) {
		super(width, height, title);
		items = new ArrayList<>();
		transformation = new Transformation();
		camera = new Camera(new Vector3f(), new Vector3f());
		mouse = new MouseInput();
		pointLights = new ArrayList<>();
		soundManager = new AudioManager();
		keyboard = new KeyboardInput();
	}
	
	@Override
	public void init() {
		try {
			soundManager.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.init();
		
		mouse.init(this);
		keyboard.init(this);
	}
	
	@Override
	public void createWindow() {
		super.createWindow();
		long cursorPointer = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
		GLFW.glfwSetCursor(this.getPointer(), cursorPointer);
		
		GLFW.glfwSetInputMode(getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	@Override
	protected void input() {
		super.input();
		mouse.input(this);		
	}
	
	@Override
	protected void render() {

		
		super.render();
	}
	

	
}
