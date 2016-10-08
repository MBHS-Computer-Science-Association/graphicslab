package graphicslab;

import org.junit.Test;
import static org.junit.Assert.*;

public class GraphicsSystem_Test {
	
	@Test
	public void multipleInitCalls() {
		assertTrue(GraphicsSystem.init());
		assertTrue(GraphicsSystem.init());
	}
	
	@Test
	public void initAndTerminate() {
		assertTrue(GraphicsSystem.init());
		GraphicsSystem.terminate();
	}
	
	@Test
	public void verifyDefaultInactive() {
		assertFalse(GraphicsSystem.isActive());
	}
}
