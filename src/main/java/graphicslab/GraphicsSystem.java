package graphicslab;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * Wrapper for LWJGL's
 * <a href="https://javadoc.lwjgl.org/org/lwjgl/glfw/GLFW.html">GLFW</a> native
 * Java bindings which handles windowing.
 * 
 * Handles the necessary static calls in order initialize and terminate the GLFW system.
 * 
 * @author trevortknguyen
 */
public class GraphicsSystem {
	private static boolean isActive = false;

	private static GLFWErrorCallback errorCallback;

	private static List<Window> windowList;
	
	static {
		windowList = new LinkedList<>();
	}
	
	/**
	 * Creates and sets the GLFW error callback to {@link System.err}.
	 * Initializes the GLFW library.
	 * 
	 * <b>This is required before the graphicslab windowing system is able to be
	 * used.</b>
	 *
	 * Returns true if successful or throws {@link IllegalStateException} in
	 * case GLFW failed to initialize.
	 */
	protected static boolean init() {
		if (isActive()) {
			return true;
		}

		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!(isActive = glfwInit())) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		return true;
	}

	protected static void addWindow(Window window) {
		if (!isActive()) {
			init();
		}
		windowList.add(window);
	}
	
	protected static void removeWindow(Window window) {
		windowList.remove(window);
		if (windowList.isEmpty() && isActive()) {
			terminate();
		}
	}
	
	/**
	 * Destroys any remaining windows or cursors. Frees the error callback. Once
	 * this function is called, you must call init() again to use any of the
	 * windowing functions.
	 * 
	 * Precondition:  none
	 * Postcondition: the glfw callback will be freed
	 * 				  the callback field will be set to null
	 *                the glfw system will be terminated
	 *                the variable will be set to inactive
	 */
	protected static void terminate() {
		glfwTerminate();

		GLFWErrorCallback previousCallback = glfwSetErrorCallback(null);
		if (previousCallback != null) {
			previousCallback.free();
		}

		errorCallback = null;
		isActive = false;
	}

	/**
	 * @return if graphics system is currently initialized
	 */
	public static boolean isActive() {
		return isActive;
	}
}
