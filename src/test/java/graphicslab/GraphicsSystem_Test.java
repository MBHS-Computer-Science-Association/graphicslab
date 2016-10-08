package graphicslab;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphicsSystem_Test {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void multipleInitCalls() {
		exception.expect(UnsupportedOperationException.class);
		GraphicsSystem.init();
		GraphicsSystem.init();
	}
	
	@Test
	public void initAndTerminate() {
		GraphicsSystem.init();
		GraphicsSystem.terminate();
	}
	
	@Test
	public void terminateWithoutInit() {
		exception.expect(UnsupportedOperationException.class);
		GraphicsSystem.terminate();
	}
}
