package graphicslab.window.input;

import graphicslab.window.Window;

public interface WindowInput {
	/**
	 * Initializes the input object to monitor a specific window.
	 * @param window the window from which input is received
	 */
	public void init(Window window);
}
