#version 140

in vec2 position;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform vec2 offsets1;
uniform vec2 offsets2;
uniform vec2 texCoordsInfo;

void main(void){

    vec2 texCoords = position + vec2(0.5, 0.5);
    texCoords.y = 1.0 - texCoords.y;
    texCoords /= texCoordsInfo.x;
    textureCoords1 = texCoords + offsets1;
    textureCoords2 = texCoords + offsets2;
    blend = texCoordsInfo.y;

    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}