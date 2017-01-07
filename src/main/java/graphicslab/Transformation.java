package graphicslab;


import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Transformation {

    private final Matrix4f projectionMatrix;
    
    private final Matrix4f ortho2DProjectionMatrix;

    private final Matrix4f worldMatrix;
    
    private final Matrix4f viewMatrix;
    
    private final Matrix4f modelViewMatrix;

    private final Matrix4f orthoProjectionMatrix;

    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        ortho2DProjectionMatrix = new Matrix4f();
        orthoProjectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f(); 
        modelViewMatrix = new Matrix4f(); 
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;        
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }
    
    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        ortho2DProjectionMatrix.identity();
        ortho2DProjectionMatrix.ortho2D(left, right, bottom, top);
        return ortho2DProjectionMatrix;
    }
    
    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        orthoProjectionMatrix.identity();
        orthoProjectionMatrix.ortho(left, right, bottom, top, zNear, zFar);
        return orthoProjectionMatrix;
    }

    @Deprecated
    public final Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
        worldMatrix.identity().translate(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }
    
    public final Matrix4f getModelViewMatrix(Item item, Matrix4f viewMatrix) {
        Vector3f rotation = item.getRotation();
        modelViewMatrix.identity().translate(item.getPosition()).
            rotateX((float)Math.toRadians(-rotation.x)).
            rotateY((float)Math.toRadians(-rotation.y)).
            rotateZ((float)Math.toRadians(-rotation.z)).
            scale(item.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }
    
    public final Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        
        viewMatrix.identity();

        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
        
    }
}