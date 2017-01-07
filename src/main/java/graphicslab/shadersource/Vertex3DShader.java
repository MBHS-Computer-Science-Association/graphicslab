package graphicslab.shadersource;

public class Vertex3DShader {
    protected static CharSequence source = ""
            + "#version 330\n"

            + "layout (location = 0) in vec3 position;"
            + "layout (location = 1) in vec2 texCoord;"
            + "layout (location = 2) in vec3 vertexNormal;"

            + "out vec2 outTexCoord;"
            + "out vec3 mvVertexPos;"
            + "out vec3 mvVertexNormal;"

            + "uniform mat4 projectionMatrix;"
            + "uniform mat4 modelViewMatrix;"

            + "void main()"
            + "{"
            + "    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);"
            + "    gl_Position = projectionMatrix * mvPos;"
            + "    outTexCoord = texCoord;"
            + "    mvVertexPos = mvPos.xyz;"
            + "    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;"
            + "}";


}
