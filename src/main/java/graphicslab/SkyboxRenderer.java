package graphicslab;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import graphicslab.shadersource.GLSLResources;

public class SkyboxRenderer extends Renderer {
    
    
    @Override
    public void init() {
        super.init();

        ShaderProgram shaderProgram = null;
        
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(GLSLResources.getSkyboxVertexSource());
            shaderProgram.createFragmentShader(GLSLResources.getSkyboxFragmentSource());
            shaderProgram.link();
            super.setShaderProgram(shaderProgram);
            
            shaderProgram.createUniform("modelViewMatrix");
            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("texture_sampler");
            shaderProgram.createUniform("ambientLight");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void render(Scene3D scene, Renderer3D renderer) {
        Skybox skybox = scene.getSkybox();
        
        getShaderProgram().bind();
        

        getShaderProgram().setUniform("texture_sampler", 0);


        // Update projection Matrix
        Matrix4f projectionMatrix = getTransformation().getProjectionMatrix(renderer.FOV, scene.getWidth(), scene.getHeight(), renderer.Z_NEAR, renderer.Z_FAR);
        getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
        Matrix4f viewMatrix = getTransformation().getViewMatrix(renderer.getCamera());
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = getTransformation().getModelViewMatrix(skybox, viewMatrix);
        getShaderProgram().setUniform("modelViewMatrix", modelViewMatrix);
        getShaderProgram().setUniform("ambientLight", scene.getSceneLighting().getAmbientLight());

        skybox.getMesh().render();
        
        getShaderProgram().unbind();
    }
}
