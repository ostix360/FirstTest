package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class WaterShader extends ShaderProgram {

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;

    public WaterShader() {
        super("water.vert", "water.frag");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = super.getUniformLocation("reflectionTexture");
        location_refractionTexture = super.getUniformLocation("refractionTexture");
    }

    public void connectTextureUnits() {
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrixToUniform(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrixToUniform(location_viewMatrix, viewMatrix);
    }

    public void loadModelMatrix(Matrix4f modelMatrix) {
        super.loadMatrixToUniform(location_modelMatrix, modelMatrix);
    }
}
