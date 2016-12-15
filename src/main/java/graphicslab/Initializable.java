package graphicslab;

/**
 * Useful for GLFW, OpenGL, and OpenAL objects that require the use of API calls which can not
 * be used until other APIs are initialized and therefore can not be included in the Java object constructor.
 * 
 * @author Trevor Thai Kim Nguyen
 *
 */
public interface Initializable {
	public void init();
}
