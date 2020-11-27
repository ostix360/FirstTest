package fr.ostix.game.graphics.shadows;

import fr.ostix.game.graphics.shader.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;


public class ShadowShader extends ShaderProgram {

    private int location_mvpMatrix;

    protected ShadowShader() {
        super("shadow.vert", "shadow.frag");
    }

    @Override
    protected void getAllUniformLocations() {
        location_mvpMatrix = super.getUniformLocation("mvpMatrix");

    }

    protected void loadMvpMatrix(Matrix4f mvpMatrix) {
        super.loadMatrixToUniform(location_mvpMatrix, mvpMatrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "in_position");
        super.bindAttribute(1, "in_textureCoords");
    }

}
