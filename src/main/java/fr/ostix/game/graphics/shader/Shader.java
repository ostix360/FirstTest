package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class Shader extends ShaderProgram {

    private static final int MAX_LIGHTS = 2;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColour;
    private int[] location_lightAttenuation;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;
    private int location_inverseNormal;
    private int location_plane;


    public Shader() {
        super("defaultShader.vert", "defaultShader.frag");
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
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColour = super.getUniformLocation("skyColour");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
        location_inverseNormal = super.getUniformLocation("inverseNormal");
        location_plane = super.getUniformLocation("plane");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_lightAttenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_lightAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadClipPlane(Vector4f value) {
        super.loadVerctor4fToUniform(location_plane, value);
    }

    public void loadInverseNormal(boolean value) {
        super.loadBooleanToUniform(location_inverseNormal, value);
    }

    public void loadOffset(float x, float y) {
        super.loadVerctor2fToUniform(location_offset, new Vector2f(x, y));
    }

    public void loadNumbuerOfRows(float numberOfRows) {
        super.loadFloatToUniform(location_numberOfRows, numberOfRows);
    }

    public void loadSkyColour(Color colour) {
        super.loadVerctor3fToUniform(location_skyColour, colour.getVec3f());
    }

    public void loadFakeLightingVariable(boolean isFakeLighting) {
        super.loadBooleanToUniform(location_useFakeLighting, isFakeLighting);
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

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrixToUniform(location_projectionMatrix, projection);
    }
}
