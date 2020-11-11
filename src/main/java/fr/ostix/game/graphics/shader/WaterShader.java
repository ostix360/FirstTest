package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Light;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class WaterShader extends ShaderProgram {

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosisition;
    private int location_nomralMap;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_depthMap;

    public WaterShader() {
        super("water.vert", "water.frag");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_reflectionTexture = super.getUniformLocation("reflectionTexture");
        location_refractionTexture = super.getUniformLocation("refractionTexture");
        location_dudvMap = super.getUniformLocation("dudvMap");
        location_moveFactor = super.getUniformLocation("moveFactor");
        location_cameraPosisition = super.getUniformLocation("cameraPosisition");
        location_nomralMap = super.getUniformLocation("nomralMap");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_depthMap = super.getUniformLocation("dpethMap");
    }

    public void connectTextureUnits() {
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_nomralMap, 3);
        super.loadInt(location_depthMap, 4);
    }

    public void loadLight(Light sun) {
        super.loadVerctor3fToUniform(location_lightPosition, sun.getPosition());
        super.loadVerctor3fToUniform(location_lightColor, sun.getColourVec3f());
    }

    public void loadWaveFactor(float move) {
        super.loadFloatToUniform(location_moveFactor, move);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrixToUniform(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrixToUniform(location_viewMatrix, viewMatrix);
        super.loadVerctor3fToUniform(location_cameraPosisition, camera.getPosition());
    }

    public void loadModelMatrix(Matrix4f modelMatrix) {
        super.loadMatrixToUniform(location_modelMatrix, modelMatrix);
    }
}
