package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class Mesh {
	private final int vaoId;
	private final List<Integer> vboIdList;
	
	private final int vertexCount;
	
	public Mesh(float[] positions, float[] colors, int[] indices) {
		vboIdList = new ArrayList<>();
		
		vertexCount = indices.length;
		
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		int positionVboId = glGenBuffers();
		vboIdList.add(positionVboId);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(positions.length);
		verticesBuffer.put(positions).flip();
		glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		

		int colorVboId = glGenBuffers();
		vboIdList.add(colorVboId);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorBuffer.put(colors).flip();
		glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		
		int indexVboId = glGenBuffers();
		vboIdList.add(indexVboId);
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void render() {
	    // Bind to the VAO
	    glBindVertexArray(vaoId);
	    
	    glEnableVertexAttribArray(0); // stores position
	    glEnableVertexAttribArray(1); // stores color

	    glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

	    // Restore state
	    glDisableVertexAttribArray(0);
	    glBindVertexArray(0);
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // TODO: Add texture cleanup when implemented.
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
	}
}
