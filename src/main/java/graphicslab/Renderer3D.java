package graphicslab;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import graphicslab.lighting.PointLight;
import graphicslab.lighting.PointLight.Attenuation;
import graphicslab.shadersource.GLSLResources;
import graphicslab.window.Window;

public class Renderer3D extends Renderer {
    private Camera camera;
    private SkyboxRenderer skyboxRenderer;
    
    /**
     * Field of View in Radians
     */
    final float FOV;
    final float Z_NEAR;
    final float Z_FAR;
    
    private Matrix4f viewMatrix;
    
    /**
     * 
     * @param fov field of view in degrees
     * @param zNear near clipping distance
     * @param zFar far clipping distance
     */
    public Renderer3D(float fov, float zNear, float zFar) {
        super();
        this.FOV = fov;
        this.Z_NEAR = zNear;
        this.Z_FAR = zFar;
        this.camera = new Camera(new Vector3f(), new Vector3f());
        this.skyboxRenderer = new SkyboxRenderer();
    }
    
    public Renderer3D() {
        this((float)Math.toRadians(60.0f), 0.01f, 2000.f);
    }
    
    @Override
    public void init() {
        super.init();

        
        ShaderProgram shaderProgram = null;
        
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(GLSLResources.get3DVertexSource());
            shaderProgram.createFragmentShader(GLSLResources.get3DFragmentSource());
            shaderProgram.link();
            super.setShaderProgram(shaderProgram);
            
            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.createUniform("modelViewMatrix");
            shaderProgram.createUniform("texture_sampler");
            shaderProgram.createMaterialUniform("material");
            
            shaderProgram.createUniform("specularPower");
            shaderProgram.createUniform("ambientLight");
            shaderProgram.createPointLightListUniform("pointLights", 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        skyboxRenderer.init();
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    @Deprecated
    public void begin(Window window, Scene3D scene) {
        super.begin();

        Matrix4f projectionMatrix = getTransformation().getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        
        getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
        getShaderProgram().setUniform("texture_sampler", 0);

        getShaderProgram().setUniform("ambientLight", scene.getSceneLighting().getAmbientLight());
        getShaderProgram().setUniform("specularPower", scene.getSceneLighting().getSpecularPower());
        
        Matrix4f viewMatrix = getTransformation().getViewMatrix(camera);
        this.viewMatrix = viewMatrix;
        
        List<PointLight> pointLights = scene.getSceneLighting().getPointLights();
        
        for (int i = 0; i < pointLights.size(); i++) {
            PointLight currPointLight = new PointLight(pointLights.get(i));
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            getShaderProgram().setUniform("pointLights", currPointLight, i);       
        }
    }
    
    public void render(Scene3D scene) {
        if (scene.getSkybox() != null) {            
            skyboxRenderer.render(scene, this);
        }
        
        getShaderProgram().bind();
        
        Matrix4f projectionMatrix = getTransformation().getProjectionMatrix(FOV, scene.getWidth(), scene.getHeight(), Z_NEAR, Z_FAR);
        
        getShaderProgram().setUniform("projectionMatrix", projectionMatrix);
        getShaderProgram().setUniform("texture_sampler", 0);

        getShaderProgram().setUniform("ambientLight", scene.getSceneLighting().getAmbientLight());
        getShaderProgram().setUniform("specularPower", scene.getSceneLighting().getSpecularPower());
        
        Matrix4f viewMatrix = getTransformation().getViewMatrix(camera);

        final List<PointLight> pointLights = scene.getSceneLighting().getPointLights(); 
        
        for (int i = 0; i < pointLights.size(); i++) {
            PointLight currPointLight = new PointLight(pointLights.get(i));
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            getShaderProgram().setUniform("pointLights", currPointLight, i);       
        }
        
        for (Item item : scene.getItems()) {
            Matrix4f modelViewMatrix = getTransformation().getModelViewMatrix(item, viewMatrix);
            
            getShaderProgram().setUniform("modelViewMatrix", modelViewMatrix);
            getShaderProgram().setUniform("material", item.getMesh().getMaterial());
            
            item.getMesh().render();
        }
        
        getShaderProgram().unbind();
        
    }
    
    @Deprecated
    public void render(Item item) {
        Matrix4f modelViewMatrix = getTransformation().getModelViewMatrix(item, viewMatrix);
        
        getShaderProgram().setUniform("modelViewMatrix", modelViewMatrix);
        getShaderProgram().setUniform("material", item.getMesh().getMaterial());
        
        item.getMesh().render();
    }
}
