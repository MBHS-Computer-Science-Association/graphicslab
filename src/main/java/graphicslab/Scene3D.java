package graphicslab;

import java.util.LinkedList;
import java.util.List;

import graphicslab.lighting.PointLight;
import graphicslab.lighting.SceneLighting;

public class Scene3D {
    private List<Item> items;
    
    private SceneLighting sceneLighting;
    
    private Skybox skybox;
    
    private float width;
    private float height;

    public Scene3D() {
        items = new LinkedList<>();
        sceneLighting = new SceneLighting();
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }
    
    public Skybox getSkybox() {
        return skybox;
    }


    public SceneLighting getSceneLighting() {
        return sceneLighting;
    }
    
    
    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
}
