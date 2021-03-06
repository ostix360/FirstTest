#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[2];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

layout(location = 0) out vec4 out_Color;
layout(location = 1) out vec4 out_BrightColor;

uniform sampler2D textureEntity;
uniform sampler2D specularMap;
uniform float useSpecularMap;
uniform sampler2D shadowMap;

uniform vec3 lightColour[2];
uniform vec3 attenuation[2];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

const float levels = 5;

const int pcfCount = 3;
const float totalsTexels = (pcfCount * 2 - 1) * (pcfCount * 2 - 1);

void main() {

    float shadowMapSize = 8192;
    float texelSize = 1/shadowMapSize;
    float total = 0.0;

    for (int x =-pcfCount; x <= pcfCount; x++){
        for (int y =-pcfCount; y <= pcfCount; y++){
            float objectNearstLght = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
            if (shadowCoords.z > objectNearstLght+0.003){
                total += 1.0;
            }
        }
    }
    total /= totalsTexels;

    float lightFactor = 0.8 -  (clamp(total, 0.0, 0.5) * shadowCoords.w);


    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular= vec3(0.0);

    for (int i = 0;i<2;i++){
        float distance = length(toLightVector[i]);
        float attenuationFactor = max(attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance), 1.0);
        vec3 unitLightVector = normalize(toLightVector[i]);

        float nDotl = dot(unitNormal, unitLightVector);
        float brightness = max(nDotl, 0.0);
        //float level = floor(brightness * levels);
        //brightness = level/levels;

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        //level = floor(dampedFactor * levels);
        //dampedFactor = level/levels;
        totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attenuationFactor;
        totalSpecular = totalSpecular + max(vec3(0.), (dampedFactor * lightColour[i] * reflectivity))/attenuationFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.1)*lightFactor;

    vec4 textureColor = texture(textureEntity, pass_textureCoords);
    if (textureColor.a < 0.5){
        discard;
    }

    out_BrightColor = vec4(0.0);
    if(useSpecularMap > 0.5){
        vec4 mapInfo = texture(specularMap, pass_textureCoords);
        totalSpecular *= mapInfo.r;
        if (mapInfo.g > 0.5){
            out_BrightColor = textureColor + vec4(totalSpecular, 1.0);
            totalDiffuse = vec3(1.0);
        }
    }

    out_Color =  vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
    out_BrightColor = vec4(0.0);
}
