package graphicslab;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import javax.swing.JFrame;

/**
 * Represents a window on the screen similar to {@link JFrame}. Provides an
 * object wrapper for the Window pointer in GLFW.
 * 
 * @author trevortknguyen
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

	/**
	 * Creates the window and allows it to be manipulated.
	 * {@link GraphicsSystem#init()} must be called and {@link GraphicsSystem}
	 * must be active for this to work.
	 * 
	 * The window is not visible by default. It must be set visible using
	 * {@link Window#showWindow() showWindow()}
	 */
	public void createWindow() {
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
	 * Determines if an operation is being performed on the main thread. Ensures fast-failing behavior, however in GLFW3, the API calls fail silently.
	 */
	private void checkThreadSafety() {
		if (Thread.currentThread() != mainThread) {
			throw new UnsupportedOperationException("Can not manipulate the window from a thread different from the main thread.");
		}
	}
	
	/**
	 * Retrieve the window pointer for debugging purposes.
	 * @return the window pointer
	 */
	protected long getWindowHandle() {
		return windowHandle;
	}
}
