#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[2];
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[2];
uniform float useFakeLighting;
uniform float inverseNormal;

uniform mat4 toShadowMapSpace;

uniform float numberOfRows;
uniform vec2 offset;

uniform vec4 plane;

const float density = 0.00245;
const float gradient = 2.85;

const float shadowDistance = 2000.0;
const float transitionDistance = 1.0;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    shadowCoords = toShadowMapSpace * worldPosition;

    gl_ClipDistance[0] = dot(worldPosition, plane);

    vec4 relativePositionToCamera =  viewMatrix * worldPosition;
    gl_Position = projectionMatrix * relativePositionToCamera;
    pass_textureCoords = (textureCoords/numberOfRows)+offset;

    vec3 actualNormal = normal;
    if (useFakeLighting == 1){
        actualNormal = vec3(0.0, 1.0, 0.0);
    }
    if (inverseNormal == 1){
        actualNormal = -actualNormal;
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    for (int i = 0; i < 2; i++){
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz -worldPosition.xyz;

    float distance = length(relativePositionToCamera.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);

    distance -= shadowDistance - transitionDistance;
    distance /= transitionDistance;
    shadowCoords.w = clamp(1.0-distance, 0.0, 1.0);
}