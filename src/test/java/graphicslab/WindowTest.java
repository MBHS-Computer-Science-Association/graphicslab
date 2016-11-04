package graphicslab;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lwjgl.glfw.GLFW;

public class WindowTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void windowCreationAndDestroyOnDifferentThread() {
		exception.expect(UnsupportedOperationException.class);
		exception.expectMessage("Can not manipulate the window from a thread different from the main thread.");
		
		// Mandatory initialization of GraphicsSystem.
		GraphicsSystem.init();
		
		Window window = new Window(800, 600, "Testing Window");
		
		// Designate window to be created on second thread.
		Thread windowCreateThread = new Thread(() -> {
			synchronized (Thread.currentThread()) {
				window.createWindow();
				window.showWindow();
				Thread.currentThread().notify();
			}

			// Keep window open for testing.
			while (GLFW.glfwWindowShouldClose(window.getWindowHandle())) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "CREATE_WINDOW");
		
		// Wait for window to be created before destruction.
		synchronized (windowCreateThread) {
			try {
				System.out.println("Waiting for window to be created.");
				windowCreateThread.start();
				windowCreateThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("Window created!");
		window.destroyWindow();
		
		
		// Mandatory termination of GraphicsSystem
		GraphicsSystem.terminate();
	}
}
