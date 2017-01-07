package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.opengl.GL;

import graphicslab.window.RenderRoutine;
import graphicslab.window.Window;

public class Renderer {
	private ShaderProgram shaderProgram;

	private final Transformation transformation;
	
	public Renderer() {
		transformation = new Transformation();
	}
	
	public void init() {
        GL.createCapabilities();
        
	    glEnable(GL_DEPTH_TEST);
	    glEnable(GL_ALPHA_TEST);
	    
	    // Workaround for the formatter bug in LWJGL 3.0.0
	    glGetError();
	    
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Deprecated
	public void render(Window window, RenderRoutine renderRoutine) {
		shaderProgram.bind();
		
		if (renderRoutine != null) {			
			renderRoutine.loop(window);
		}
		
		shaderProgram.unbind();
	}
	
	@Deprecated
	public void begin() {
        shaderProgram.bind();
	}
	
	@Deprecated
	public void end() {
        shaderProgram.unbind();
	}
	
	public Transformation getTransformation() {
	    return transformation;
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
}
