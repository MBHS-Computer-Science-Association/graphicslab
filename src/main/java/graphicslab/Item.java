package graphicslab;

import org.joml.Vector3f;

public class Item {
	private Mesh mesh;
	
	private final Vector3f position;
	private final Vector3f rotation; 
	private float scale;

    public Item() {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        scale = 1.0f;
    }
	
	public Item(Mesh mesh) {
	    this();
	    setMesh(mesh);
	}
	
	public void setMesh(Mesh mesh) {
	    this.mesh = mesh;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation.x = rotation.x;
		this.rotation.y = rotation.y;
		this.rotation.z = rotation.z;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
}
