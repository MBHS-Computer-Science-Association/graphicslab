package graphicslab;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import javax.swing.JFrame;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GL;

/**
 * Represents a window on the screen similar to {@link JFrame}. Provides an
 * object wrapper for the Window pointer in GLFW.
 * 
 * @author Trevor Thai Kim Nguyen
 */
public class Window {
	private static final int OPENGL_VERSION_MAJOR = 3;
	private static final int OPENGL_VERSION_MINOR = 2;

	/**
	 * The thread on which the window was created. This is considered the main
	 * thread because most functions don't work on the window if the window is
	 * manipulated from a thread different from the main thread.
	 */
	private Thread mainThread;

	private long windowHandle;

	private int width;
	private int height;
	private String title;

	private GLFWKeyCallbackI keycallback;
	
	private RenderRoutine render;
	private StateRoutine state;
	private InputRoutine input;
	
	/**
	 * Constructs and creates the window on the current thread. Window must be
	 * initialized on the thread on which it will be manipulated.
	 * 
	 * In order to construct, but not create a new window, use
	 * {@link Window#Window(boolean)} and specify the boolean as false. Do this
	 * if you plan to manipulate the window on a different thread from
	 * construction.
	 * 
	 * @param width
	 *            in pixels
	 * @param height
	 *            in pixels
	 * @param title
	 *            the text that appears in the top bar of the window
	 */
	public Window(int width, int height, String title) {
		this(width, height, title, true);
	}

	/**
	 * Constructs the window object.
	 * 
	 * @param width
	 *            in pixels
	 * @param height
	 *            in pixels
	 * @param title
	 *            the text that appears in the top bar of the window
	 * @param createWindow
	 *            if the window should be created
	 */
	public Window(int width, int height, String title, boolean createWindow) {
		this.width = width;
		this.height = height;
		this.title = title;
		
		if (createWindow) {
			createWindow();
		}
	}
	
	public void setRenderRoutine(RenderRoutine routine) {
		render = routine;
	}
	
	public void setInputRoutine(InputRoutine routine) {
		input = routine;
	}
	
	public void setStateRoutine(StateRoutine routine) {
		state = routine;
	}

	/**
	 * Creates the window and allows it to be manipulated.
	 * {@link GraphicsSystem#init()} must be called and {@link GraphicsSystem}
	 * must be active for this to work.
	 * 
	 * The window is not visible by default. It must be set visible using
	 * {@link Window#showWindow() showWindow()}
	 */
	public void createWindow() {
		GraphicsSystem.addWindow(this);
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, OPENGL_VERSION_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, OPENGL_VERSION_MINOR);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

		if (windowHandle == NULL) {
			throw new RuntimeException("Unable to create GLFW window.");
		}

		glfwMakeContextCurrent(windowHandle);
		glfwSwapInterval(1);
		glfwMakeContextCurrent(NULL);

		mainThread = Thread.currentThread();
	}


	public void startLoop() {
		keycallback = (window, key, scancode, action, mods)  -> {};
		
		glfwSetKeyCallback(windowHandle, keycallback);
		
		glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		double msecsPerFrame = 1000 / 50.0;
		double msecsPerUpdate = 1000 / 30.0;
		double previous = glfwGetTime();
		double steps = 0.0;
		
		while (!glfwWindowShouldClose(windowHandle)) {
			double loopStart = glfwGetTime();
			double elapsed = loopStart - previous;
			previous = loopStart;
			steps += elapsed;
			
			input();
			gameState();
			render();

			while (steps >= msecsPerUpdate) {
				gameState();
				steps -= msecsPerUpdate;
			}
			sync(loopStart);


		}

		destroyWindow();
	}
	
	/**
	 * Determines if a key is being pressed while the window is active.
	 * @param keycode the GLFW codes for keys
	 * @return true if the key is being pressed
	 */
	public boolean isKeyPressed(int keycode) {
		return glfwGetKey(windowHandle, keycode) == GLFW.GLFW_PRESS;
	}

	private void input() {
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
		
		if (input != null) {			
			input.processInput();
		}
	}

	private void gameState() {
		if (state != null) {			
			state.updateState(this);
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		if (render != null) {			
			render.loop(this);
		}

		glfwSwapBuffers(windowHandle); // swap the color buffers
	}

	/**
	 * Waits an appropriate amount of time before doing the next rendering sequence. This prevents the graphics application from running too quickly.
	 * @param loopStartTime the time in milliseconds when the loop when the loop was started.
	 */
	private void sync(double loopStartTime) {
		float loopSlot = 1f / 50;
		double endTime = loopStartTime + loopSlot;
		while (glfwGetTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Destroys the window. It will cease to be visible and can not be
	 * manipulated after this is called. However, the window can still be
	 * created again using {@link Window#createWindow() createWindow()} after
	 * the window is destroyed.
	 */
	public void destroyWindow() {
		checkThreadSafety();
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);
		
		GraphicsSystem.removeWindow(this);
	}

	/**
	 * Sets the window title.
	 * 
	 * @param title
	 *            the text that appears in the top bar of the window
	 */
	public void setWindowTitle(String title) {
		checkThreadSafety();
		glfwSetWindowTitle(windowHandle, title);
	}

	/**
	 * Makes the window visible.
	 */
	public void showWindow() {
		checkThreadSafety();
		glfwShowWindow(windowHandle);
	}

	/**
	 * Makes the window invisible.
	 */
	public void hideWindow() {
		checkThreadSafety();
		glfwHideWindow(windowHandle);
	}

	/**
	 * Determines if an operation is being performed on the main thread. Ensures
	 * fast-failing behavior, however in GLFW3, the API calls fail silently.
	 */
	private void checkThreadSafety() {
		if (Thread.currentThread() != mainThread) {
			throw new UnsupportedOperationException(
					"Can not manipulate the window from a thread different from the main thread.");
		}
	}

	/**
	 * Retrieve the window pointer for debugging purposes.
	 * 
	 * @return the window pointer
	 */
	protected long getWindowHandle() {
		return windowHandle;
	}
}
