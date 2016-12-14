package graphicslab;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private final Vector3f position;
	private final Vector3f rotation;
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }
    
    public Vector3f getRotation() {
    	return rotation;
    }
    
    public void setRotation(float x, float y, float z) {
    	rotation.x = x;
    	rotation.y = y;
    	rotation.z = z;
    }
    
    public void moveRotation(float xoffset, float yoffset, float zoffset) {
    	rotation.x += xoffset;
    	rotation.y += yoffset;
    	rotation.z += zoffset;
    }
}
