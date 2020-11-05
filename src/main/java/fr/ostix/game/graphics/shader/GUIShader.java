package fr.ostix.game.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GUIShader extends ShaderProgram {
    private int location_transformationMatrix;

    public GUIShader() {
        super("guiShader.vert", "guiShader.frag");
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
