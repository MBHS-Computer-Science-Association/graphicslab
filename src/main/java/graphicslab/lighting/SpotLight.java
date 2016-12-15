package graphicslab.lighting;

import org.joml.Vector3f;

public class SpotLight {
	private PointLight pointLight;
	private Vector3f coneDirection;
	private float cutOff;
	
	public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        this.cutOff = cutOffAngle;
    }
	
	public SpotLight(SpotLight spotLight) {
		this(new PointLight(spotLight.pointLight), new Vector3f(spotLight.coneDirection), spotLight.cutOff);
	}
	
	public PointLight getPointLight() {
		return pointLight;
	}
	
	public Vector3f getConeDirection() {
		return coneDirection;
	}
	
	public float getCutOff() {
		return cutOff;
	}
}