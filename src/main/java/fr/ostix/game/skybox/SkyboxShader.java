package fr.ostix.game.skybox;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.shader.ShaderProgram;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class SkyboxShader extends ShaderProgram {


    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader() {
        super("skyboxShader.vert", "skyboxShader.frag");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrixToUniform(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
