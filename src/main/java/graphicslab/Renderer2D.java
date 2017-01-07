package graphicslab;


import org.joml.Matrix4f;

import graphicslab.shadersource.GLSLResources;
import graphicslab.window.Window;

public class Renderer2D extends Renderer {
    
    private Matrix4f orthoView;

    @Override
    public void init() {
        super.init();

        ShaderProgram shaderProgram = null;
        
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(GLSLResources.get2DVertexSource());
            shaderProgram.createFragmentShader(GLSLResources.get2DFragmentSource());
            shaderProgram.link();
            super.setShaderProgram(shaderProgram);
            
            shaderProgram.createUniform("projModelMatrix");
            shaderProgram.createUniform("texture_sampler");
            shaderProgram.createUniform("color");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void begin(Window window) {
        super.begin();

        // this.orthoView = transformation.getOrthoProjectionMatrix(0,
        // window.getWidth(), 0, window.getHeight(), 100, -100);
        this.orthoView = getTransformation().getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0, 100,
                -100);
    }

    public void render(Item item) {
        Matrix4f projModelMatrix = getTransformation().getModelViewMatrix(item, orthoView);

        getShaderProgram().setUniform("projModelMatrix", projModelMatrix);
        getShaderProgram().setUniform("color", item.getMesh().getMaterial().getColor());

        item.getMesh().render();
    }
}
