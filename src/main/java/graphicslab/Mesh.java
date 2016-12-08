package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class Mesh implements Loadable {
	private int vaoId;
	private final List<Integer> vboIdList;
	
	private Texture texture;
	
	private final int vertexCount;
	
	private FloatBuffer verticesBuffer;
	private FloatBuffer textureCoordsBuffer;
	private IntBuffer indicesBuffer;
	
	private boolean loaded;
	
	public Mesh(float[] positions, float[] textureCoords, int[] indices) {
		loaded = false;
		
		vboIdList = new ArrayList<>();
		vertexCount = indices.length;
		
		verticesBuffer = BufferUtils.createFloatBuffer(positions.length);
		verticesBuffer.put(positions).flip();
		
		textureCoordsBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
		textureCoordsBuffer.put(textureCoords).flip();
				
		indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
	}
	
	@Override
	public void load() {
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		int positionVboId = glGenBuffers();
		vboIdList.add(positionVboId);
		glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		int textureCoordsVboId = glGenBuffers();
		vboIdList.add(textureCoordsVboId);
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordsVboId);
		glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		int indexVboId = glGenBuffers();
		vboIdList.add(indexVboId);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		// Unbinds Buffers and VertexArrays
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		verticesBuffer = null;
		textureCoordsBuffer = null;
		indicesBuffer = null;
		
		loaded = true;
	}
	
	public void render() {
		if (!isLoaded()) {
			load();
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getId());

		// Bind to the VAO
	    glBindVertexArray(vaoId);
	    
	    glEnableVertexAttribArray(0); // stores position
	    glEnableVertexAttribArray(1); // stores color

	    
	    glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

	    // Restore state
	    glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
	    glBindVertexArray(0);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void cleanup() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        texture.cleanup();
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
	}
}
