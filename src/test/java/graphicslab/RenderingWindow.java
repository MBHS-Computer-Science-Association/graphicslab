package graphicslab;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class RenderingWindow extends Window{
	List<Item> items;
	Transformation transformation;
	Camera camera;
	MouseInput mouse;

	public RenderingWindow(int width, int height, String title) {
		super(width, height, title);
		items = new ArrayList<>();
		transformation = new Transformation();
		camera = new Camera(new Vector3f(), new Vector3f());
		mouse = new MouseInput();
	}
	
	@Override
	public void init() {
		super.init();
		
		mouse.init(this);
	}
	
	@Override
	protected void input() {
		super.input();
		mouse.input(this);		
	}
}
