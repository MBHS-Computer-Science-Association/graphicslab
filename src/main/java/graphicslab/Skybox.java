package graphicslab;

import java.io.IOException;

import org.joml.Vector3f;

import graphicslab.lighting.Material;

public class Skybox extends Item {
    public Skybox(String objectFile, String textureFile) {
        super();
        setPosition(new Vector3f(0, 0, 0));
        Mesh skyboxMesh = null;
        try {
            skyboxMesh = OBJLoader.loadMesh(objectFile);
            Texture skyboxTexture = new Texture(textureFile);
            Material material = new Material(skyboxTexture, 1.0f);
            skyboxMesh.setMaterial(material);
            setMesh(skyboxMesh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
