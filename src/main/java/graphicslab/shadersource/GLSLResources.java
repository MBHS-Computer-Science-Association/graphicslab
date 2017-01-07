package graphicslab.shadersource;

public class GLSLResources {
    public static CharSequence get3DVertexSource() {
        return Vertex3DShader.source;
    }

    public static CharSequence get3DFragmentSource() {
        return Fragment3DShader.source;
    }

    public static CharSequence get2DVertexSource() {
        return Vertex2DShader.source;
    }
    
    public static CharSequence get2DFragmentSource() {
        return Fragment2DShader.source;
    }

    public static CharSequence getSkyboxVertexSource() {
        return VertexSkyboxShader.source;
    }

    public static CharSequence getSkyboxFragmentSource() {
        return FragmentSkyboxShader.source;
    }
    
}
