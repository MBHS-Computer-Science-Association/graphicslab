package graphicslab.shadersource;

public class Vertex2DShader {
    protected static CharSequence source = ""
            + "#version 330\n"

            + "layout (location = 0) in vec3 position;"
            + "layout (location = 1) in vec2 texCoord;"
            + "layout (location = 2) in vec3 vertexNormal;"

               // debugging
            + "out vec3 pos;"


            + "out vec2 outTexCoord;"

            + "uniform mat4 projModelMatrix;"

            + "void main()"
            + "{"
            + "    gl_Position = projModelMatrix * vec4(position, 1.0);"
            + "    outTexCoord = texCoord;"

            + "    pos = position;"
            + "}";

}
