package graphicslab;

import java.util.List;

public class RenderingWindow extends Window{

	public RenderingWindow(int width, int height, String title) {
		super(width, height, title);
	}
	

	public RenderingWindow(int width, int height, String title, boolean createWindow) {
		super(width, height, title, createWindow);
	}

	List<Item> items;
	Transformation transformation;
}
