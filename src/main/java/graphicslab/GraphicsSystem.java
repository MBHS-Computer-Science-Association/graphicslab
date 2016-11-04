package graphicslab;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;

public class GraphicsSystem {
	private static boolean isActive = false;

	private static GLFWErrorCallback errorCallback;

	public static void init() {
		if (isActive()) {
			throw new UnsupportedOperationException("Can not initialize active graphics system.");
		}
		
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!(isActive = glfwInit())) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
	}

	/**
	 * Precondition:  none
	 * Postcondition: the glfw callback will be freed
	 * 				  the callback field will be set to null
	 *                the glfw system will be terminated
	 *                the variable will be set to inactive
	 */
	public static void terminate() {
		glfwTerminate();

		GLFWErrorCallback previousCallback = glfwSetErrorCallback(null);
		if (previousCallback != null) {
			previousCallback.free();
		}

		errorCallback = null;
		isActive = false;
	}
	

	public static boolean isActive() {
		return isActive;
	}
}
