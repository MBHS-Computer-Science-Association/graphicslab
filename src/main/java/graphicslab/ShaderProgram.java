package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	private final int programId;
	
	private int vertexShaderId;
	
	private int fragmentShaderId;
	
	private boolean shadersLinked;
	
	public ShaderProgram() throws ShaderException {
		programId = glCreateProgram();
		
		if (programId == 0) {
			throw new ShaderException("Could not create shader program.");
		}
	}
	
	public int createVertexShader(CharSequence sourcecode) throws ShaderException {
		vertexShaderId = createShader(sourcecode, GL_VERTEX_SHADER);
		return vertexShaderId;
	}
	
	public int createFragmentShader(CharSequence sourcecode) throws ShaderException {
		fragmentShaderId = createShader(sourcecode, GL_FRAGMENT_SHADER);
		return fragmentShaderId;
	}
	
	 public void link() throws ShaderException {
	        glLinkProgram(programId);
	        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
	            throw new ShaderException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }

	        glValidateProgram(programId);
	        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
	            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }
	        
	        shadersLinked = true;
	    }
	
	protected int createShader(CharSequence sourcecode, int type) throws ShaderException {
		int shaderId;
		shaderId = glCreateShader(type);
		if (shaderId == 0) {
			throw new ShaderException("Can't create the shader.");
		}
		
		assert glGetShaderi(vertexShaderId, GL_SHADER_TYPE) == type;
		
		glShaderSource(shaderId, sourcecode);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			
			int maxLogLength = 1024;
			throw new ShaderException("Error compiling shader code: " + glGetShaderInfoLog(shaderId, maxLogLength));
		}
		
		glAttachShader(programId, shaderId);
		
		return shaderId;
	}
	
	public void bind() {
		assert glGetProgrami(programId, GL_LINK_STATUS) == GL_TRUE;
		glUseProgram(programId);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void cleanup() {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
	}
	
	class ShaderException extends Exception {
		private static final long serialVersionUID = -4259370241344527256L;

		public ShaderException(String message) {
			super(message);
		}
	}
}
