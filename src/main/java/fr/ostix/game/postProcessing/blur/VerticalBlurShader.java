package fr.ostix.game.postProcessing.blur;

import fr.ostix.game.graphics.shader.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram {


    private int location_targetHeight;

    protected VerticalBlurShader() {
        super("postProcessing/verticalBlur.vert", "postProcessing/blur.frag");
    }

    protected void loadTargetHeight(float height) {
        super.loadFloatToUniform(location_targetHeight, height);
    }

    @Override
    protected void getAllUniformLocations() {
        location_targetHeight = super.getUniformLocation("targetHeight");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
