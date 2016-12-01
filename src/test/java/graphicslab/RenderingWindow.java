package graphicslab;

public class RenderingWindow extends Window{

	public RenderingWindow(int width, int height, String title) {
		super(width, height, title);
	}
	

	public RenderingWindow(int width, int height, String title, boolean createWindow) {
		super(width, height, title, createWindow);
	}

	int vaoId;
	int vboId;
	
}
