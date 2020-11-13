#version 330

in vec2 pass_textureCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D textureAtlas;

void main(void){
    out_Color = vec4(color, texture(textureAtlas, pass_textureCoords).a);
}