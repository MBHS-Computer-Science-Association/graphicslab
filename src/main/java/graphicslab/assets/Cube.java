package graphicslab.assets;

import java.io.IOException;

import graphicslab.Item;
import graphicslab.Mesh;
import graphicslab.OBJLoader;
import graphicslab.Texture;
import graphicslab.lighting.Material;

public class Cube extends Item {
    private static Mesh mesh;
    
    static {
        try {
            mesh = OBJLoader.loadMesh("src/test/cube.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public Cube() {
        super(mesh);
    }
}
