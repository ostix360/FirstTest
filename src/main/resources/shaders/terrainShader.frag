#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[2];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour[2];
uniform vec3 attenuation[2];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main() {
    vec4 blendMapColour = texture(blendMap, pass_textureCoords);

    float backTextureAmount = 1 - (blendMapColour.r, blendMapColour.g, blendMapColour.b);
    vec2 tiledCoords = pass_textureCoords *40;
    vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmount;
    vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;

    vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;


    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpeculare= vec3(0.0);

    for (int i = 0;i<2;i++){
        float distance = length(toLightVector[i]);
        float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        vec3 unitLightVector = normalize(toLightVector[i]);

        float nDotl = dot(unitNormal, unitLightVector);
        float brightness = max(nDotl, 0.0);

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attenuationFactor;
        totalSpeculare = totalSpeculare + (dampedFactor * lightColour[i] * reflectivity)/attenuationFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.1);

    out_Color = vec4(totalDiffuse, 1.0) * totalColour + vec4(totalSpeculare, 1.0);
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}
