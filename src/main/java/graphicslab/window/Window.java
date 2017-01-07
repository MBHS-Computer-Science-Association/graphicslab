package graphicslab.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;

import graphicslab.window.input.InputHandler;


/**
 * Represents a window on the screen similar to {@link JFrame}. Provides an
 * object wrapper for the Window pointer in GLFW. It is equipped with limited functionality intending more advanced users
 * to take advantage of direct GLFW access through the window pointer.
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
	private static Thread mainThread;

	private long windowHandle;

	private int width;
	private int height;
	private boolean resized;
	
	private String title;

	private GLFWFramebufferSizeCallbackI sizecallback;
	
	private InitializeRoutine init;
	private RenderRoutine render;
	private StateRoutine state;
	private InputRoutine input;
	
	private Set<InputHandler> inputHandlers;
	
	/**
	 * Constructs and creates the window on the current thread. Window must be
	 * initialized on the thread on which it will be manipulated.
	 * 
	 * In order to construct, but not create a new window, use
	 * {@link Window#Window(width, height, title, createWindow)} and specify the boolean as false. Do this
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
		
		this.inputHandlers = new HashSet<>();
		
		if (createWindow) {
			createWindow();
		}
	}
	
	protected void init() {
		sizecallback = (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResized(true);
		};
		
		glfwSetFramebufferSizeCallback(windowHandle, sizecallback);
		
		glfwMakeContextCurrent(windowHandle);
		glfwSwapInterval(1);
		
		for (InputHandler handler : inputHandlers) {
		    handler.init(this);
		}
		
		if (init != null) {
			init.initialize(this);
		}
	}
	
	public void setInitializeRoutine(InitializeRoutine routine) {
		init = routine;
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
	 * {@link WindowSystem} will be notified of the window's creation in order
	 * for the window system to initialize itself if it is currently inactive.
	 * 
	 * The window is not visible by default. It must be set visible using
	 * {@link Window#showWindow() showWindow()}
	 */
	public void createWindow() {
		WindowSystem.addWindow(this);
		
		glfwDefaultWindowHints();
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, OPENGL_VERSION_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, OPENGL_VERSION_MINOR);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

		if (windowHandle == NULL) {
			throw new RuntimeException("Unable to create GLFW window.");
		}

		if (mainThread == null) {			
			mainThread = Thread.currentThread();
		}
	}

	/**
	 * Starts the window's input, state, and rendering loop. This function will continue and only return when the window is closed.
	 */
	public void startLoop() {

		init();

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

	public void addInputHandler(InputHandler handler) {
	    inputHandlers.add(handler);
	}
	
	protected void input() {
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
		
		for (InputHandler handler : inputHandlers) {
		    handler.input();
		}
		
		if (input != null) {			
			input.processInput(this);
		}
	}

	private void gameState() {
		if (state != null) {			
			state.updateState(this);
		}
	}

    protected void render() {

        if (isResized()) {
            glViewport(0, 0, getWidth(), getHeight());
            setResized(false);
        }

        if (render != null) {
            render.loop(this);
        }
        swapBuffers();
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
	
	private void swapBuffers() {
	    glfwSwapBuffers(windowHandle);
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
		
		WindowSystem.removeWindow(this);
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isResized() {
		return resized;
	}
	
	public void setResized(boolean resized) {
		this.resized = resized;
	}
	
	public void setCursorInputMode(int value) {
	    glfwSetInputMode(windowHandle, GLFW_CURSOR, value);
	}
	
	public void setWindowShouldClose(boolean value) {
	    glfwSetWindowShouldClose(windowHandle, value);
	}
	
	/**
	 * Makes the specified window visible if it was previously hidden. If the window is already visible or is in full screen mode, this function does nothing.
	 * 
	 * This function must only be called from the main thread.
	 */
	public void showWindow() {
		checkThreadSafety();
		glfwShowWindow(windowHandle);
	}

	/**
	 * Hides the specified window, if it was previously visible. If the window is already hidden or is in full screen mode, this function does nothing.
	 * 
	 * This function must only be called from the main thread.
	 */
	public void hideWindow() {
		checkThreadSafety();
		glfwHideWindow(windowHandle);
	}

	/**
	 * Determines if a function is being performed on the main thread. Ensures
	 * fast-failing behavior for functions that GLFW prevents. In GLFW3 a native code runtime error is thrown
	 * instead.
	 */
	private static void checkThreadSafety() {
		if (Thread.currentThread() != mainThread) {
			throw new UnsupportedOperationException(
					"This GLFW function can not be called from any thread other than the main thread.");
		}
	}

	/**
	 * The window pointer is used by GLFW to identify the window's identity in all of the library's static calls.
	 * It can be used to manually make GLFW API calls to change the way the window functions past basic functionality.
	 * 
	 * @return the window pointer used by GLFW for identification
	 */
	public long getPointer() {
		return windowHandle;
	}
}
