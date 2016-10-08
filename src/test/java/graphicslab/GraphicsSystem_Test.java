package graphicslab;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphicsSystem_Test {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void multipleInitializeCalls() {
		exception.expect(UnsupportedOperationException.class);
		GraphicsSystem.initialize();
		GraphicsSystem.initialize();
	}
	
	@Test
	public void initializeAndDispose() {
		GraphicsSystem.initialize();
		GraphicsSystem.dispose();
	}
	
	@Test
	public void disposeWithoutInitialize() {
		exception.expect(UnsupportedOperationException.class);
		GraphicsSystem.dispose();
	}
}
