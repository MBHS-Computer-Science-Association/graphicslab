package graphicslab;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphicsSystemTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Make sure that no test case leaves the graphics system open.
	 */
	@After
	public void cleanup() {
		GraphicsSystem.terminate();
	}
	
	/**
	 * User should not initialize the graphics system twice.
	 */
	@Test
	public void multipleInitCalls() {
		exception.expect(UnsupportedOperationException.class);
		GraphicsSystem.init();
		GraphicsSystem.init();
	}
	
	/**
	 * Simple initialization and termination.
	 */
	@Test
	public void initAndTerminate() {
		GraphicsSystem.init();
		GraphicsSystem.terminate();
	}
	
	/**
	 * Simple termination.
	 * Should be able to terminate even if not active.
	 */
	@Test
	public void terminateWithoutInit() {
		GraphicsSystem.terminate();
	}
}
