#version 330

in vec2 outTexCoord;

// debugging
in vec3 pos;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 color;

void main()
{
    fragColor = vec4(color, 1) * texture(texture_sampler, outTexCoord);
}