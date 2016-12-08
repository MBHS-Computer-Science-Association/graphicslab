package graphicslab;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture implements Loadable {
	private int textureId;

	private int width;
	private int height;
	private ByteBuffer buffer;
	
	private boolean loaded;
	
	public Texture(String filename) {
		loaded = false;
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		buffer = stbi_load(filename, w, h, comp, 4);
		
		width = w.get();
		height = h.get();
	}
	
	@Override
	public void load() {
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		buffer = null;
		
		loaded = true;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	public int getId() {
		if (!isLoaded()) {
			load();
		}
		
		return textureId;
	}
	
	public void cleanup() {
		glDeleteTextures(textureId);
	}
}
