package graphicslab.assets;

import java.io.IOException;

import graphicslab.Item;
import graphicslab.Mesh;
import graphicslab.OBJLoader;
import graphicslab.Texture;
import graphicslab.lighting.Material;

public class GrassBlock extends Item {
    private static Mesh mesh;
    
    static {
        try {
            mesh = OBJLoader.loadMesh("src/test/cube.obj");
            Texture grass = new Texture("src/test/grassblock.png");
            Material grassBlockMaterial = new Material(grass, 1f);
            mesh.setMaterial(grassBlockMaterial);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public GrassBlock() {
        super(mesh);
    }
}
