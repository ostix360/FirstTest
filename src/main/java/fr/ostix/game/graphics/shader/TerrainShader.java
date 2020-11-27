package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class TerrainShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 2;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColour;
    private int[] location_lightAttenuation;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;
    private int location_plane;
    private int location_toShadowMapSpace;
    private int location_shadowMap;

    public TerrainShader() {
        super("terrainShader.vert", "terrainShader.frag");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("shineDamper");
        location_skyColour = super.getUniformLocation("skyColour");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
        location_plane = super.getUniformLocation("plane");
        location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        location_shadowMap = super.getUniformLocation("shadowMap");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_lightAttenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_lightAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }


    public void connectTerrainUnits() {
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_rTexture, 1);
        super.loadInt(location_gTexture, 2);
        super.loadInt(location_bTexture, 3);
        super.loadInt(location_blendMap, 4);
        super.loadInt(location_shadowMap, 5);
    }


    public void loadShaderMapSpace(Matrix4f matrix) {
        super.loadMatrixToUniform(location_toShadowMapSpace, matrix);
    }

    public void loadClipPlane(Vector4f value) {
        super.loadVerctor4fToUniform(location_plane, value);
    }

    public void loadSkyColour(Color colour) {
        super.loadVerctor3fToUniform(location_skyColour, colour.getVec3f());
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloatToUniform(location_shineDamper, damper);
        super.loadFloatToUniform(location_reflectivity, reflectivity);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                Light light = lights.get(i);
                super.loadVerctor3fToUniform(location_lightPosition[i], light.getPosition());
                super.loadVerctor3fToUniform(location_lightColour[i], light.getColourVec3f());
                super.loadVerctor3fToUniform(location_lightAttenuation[i], light.getAttenuation());
            } else {
                super.loadVerctor3fToUniform(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.loadVerctor3fToUniform(location_lightColour[i], new Vector3f(0, 0, 0));
                super.loadVerctor3fToUniform(location_lightAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera cam) {
        Matrix4f viewMatrix = Maths.createViewMatrix(cam);
        super.loadMatrixToUniform(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrixToUniform(location_projectionMatrix,projection);
    }
}
