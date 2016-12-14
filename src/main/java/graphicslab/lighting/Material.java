package graphicslab.lighting;

import org.joml.Vector3f;

import graphicslab.Texture;

public class Material {
	private static Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);
	
	private Vector3f color;
	private Texture texture;
	
	private float reflectance;
	
    public Material() {
        color = DEFAULT_COLOR;
        reflectance = 0;
    }
	
	public Material(Vector3f color, float reflectance) {
		this();
		this.color = color;
		this.reflectance = reflectance;
	}
	
	public Material(Texture texture, float reflectance) {
		this();
		this.texture = texture;
		this.reflectance = reflectance;
	}
	
	public Vector3f getColor() {
		return color;
	}
	public boolean isTextured() {
		return texture != null;
	}
	
	public int getTextureId() {
		if (texture != null) {
			return texture.getId();
		} else {
			return 0;
		}
	}
	
	public float getReflectance() {
		return reflectance;
	}
}
