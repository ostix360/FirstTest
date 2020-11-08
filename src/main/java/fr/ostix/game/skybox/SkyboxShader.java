package fr.ostix.game.skybox;

import fr.ostix.game.core.DisplayManager;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.graphics.shader.ShaderProgram;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxShader extends ShaderProgram {

    private static final float ROTATE_SPEED = 3f;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_Texture1;
    private int location_Texture2;
    private int location_blendFactor;

    private float rotate;

    public SkyboxShader() {
        super("skyboxShader.vert", "skyboxShader.frag");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        rotate += ROTATE_SPEED * DisplayManager.getFrameTimeSeconde();
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        matrix.rotate((float) Math.toRadians(rotate), new Vector3f(0, 1, 0));
        super.loadMatrixToUniform(location_viewMatrix, matrix);
    }

    public void connectTextureUnits() {
        super.loadInt(location_Texture1, 0);
        super.loadInt(location_Texture2, 1);
    }

    public void loadBlendFactor(float factor) {
        super.loadFloatToUniform(location_blendFactor, factor);
    }

    public void loadFogColor(Color fog) {
        super.loadVerctor3fToUniform(location_fogColor, fog.getVec3f());
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_Texture1 = super.getUniformLocation("cubeMap");
        location_Texture2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("bendFactor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
