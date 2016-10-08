package graphicslab;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;

public class GraphicsSystem {
	private static boolean isActive = false;

	private static GLFWErrorCallback errorCallback;

	public static void initialize() {
		if (isActive()) {
			throw new UnsupportedOperationException("Can not initialize an already active graphics system.");
		}
		
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!(isActive = glfwInit())) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
	}

	public static void dispose() {
		if (!isActive()) {
			throw new UnsupportedOperationException("Can not dispose of inactive graphics system.");
		}
		
		glfwTerminate();

		GLFWErrorCallback previousCallback = glfwSetErrorCallback(null);
		previousCallback.free();
	}

	public static boolean isActive() {
		return isActive;
	}
}
