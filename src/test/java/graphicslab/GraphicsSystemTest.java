package graphicslab;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

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
		assertTrue(GraphicsSystem.init());
		assertTrue(GraphicsSystem.init());
	}
	
	/**
	 * Simple initialization and termination.
	 */
	@Test
	public void initAndTerminate() {
		assertTrue(GraphicsSystem.init());
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
