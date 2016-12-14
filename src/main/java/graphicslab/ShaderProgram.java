package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import graphicslab.lighting.Material;
import graphicslab.lighting.PointLight;

public class ShaderProgram {
	private final int programId;
	
	private int vertexShaderId;
	
	private int fragmentShaderId;
	
	private final Map<String, Integer> uniforms;
	
	private boolean shadersLinked;
	
	public ShaderProgram() throws ShaderException {
		programId = glCreateProgram();
		
		if (programId == 0) {
			throw new ShaderException("Could not create shader program.");
		}
		
		uniforms = new HashMap<>();
	}
	

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception ("Could not find uniform in shader program:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }
    
    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }
    
    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".useColor");
        createUniform(uniformName + ".reflectance");
    }
    
    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor() );
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".color", material.getColor() );
        setUniform(uniformName + ".useColor", material.isTextured() ? 0 : 1);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
    
    public void setUniform(String uniformName, Matrix4f value) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
    }
    
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, float value) {
    	glUniform1f(uniforms.get(uniformName), value);
    }
    
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
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
	        
	        shadersLinked = true;
	}
	 
	 public void validate() throws ShaderException {
	        glValidateProgram(programId);
	        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
	            throw new ShaderValidationException("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
	        }
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
	
	public boolean isLinked() {
		return isLinked();
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
	

	class ShaderValidationException extends RuntimeException {
		private static final long serialVersionUID = 3223687987511458613L;

		public ShaderValidationException(String message) {
			super(message);
		}
	}
}
