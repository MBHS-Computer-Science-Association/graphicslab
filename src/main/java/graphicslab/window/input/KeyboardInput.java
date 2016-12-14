package graphicslab.window.input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import graphicslab.window.Window;

public class KeyboardInput {
	private GLFWKeyCallbackI keycallback;
	
	private Window window;
	
	public KeyboardInput() {
		
	}
	
	public void init(Window window) {
		this.window = window;
		keycallback = (windowPointer, key, scancode, action, mods)  -> {};
		GLFW.glfwSetKeyCallback(window.getPointer(), keycallback);
	}
	
	/**
	 * Determines if a key is being pressed while the window is active.
	 * @param keycode the GLFW codes for keys
	 * @return true if the key is being pressed
	 */
	public boolean isKeyPressed(int keycode) {
		return glfwGetKey(window.getPointer(), keycode) == GLFW.GLFW_PRESS;
	}
}
