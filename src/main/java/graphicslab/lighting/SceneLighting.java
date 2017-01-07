package graphicslab.lighting;

import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3f;

public class SceneLighting {

    private Vector3f ambientLight;
    private List<PointLight> pointLights;
    private float specularPower;
    
    public SceneLighting() {
        pointLights = new LinkedList<>();
        ambientLight = new Vector3f(1f, 1f, 1f);
        specularPower = 1f;
    }
    
    public float getSpecularPower() {
        return specularPower;
    }
    
    public Vector3f getAmbientLight() {
        return ambientLight;
    }
    
    public List<PointLight> getPointLights() {
        return pointLights;
    }
}
