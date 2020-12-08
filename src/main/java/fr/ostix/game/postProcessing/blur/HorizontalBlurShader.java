package fr.ostix.game.postProcessing.blur;


import fr.ostix.game.graphics.shader.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {


    private int location_targetWidth;

    protected HorizontalBlurShader() {
        super("postProcessing/horizontalBlur.vert", "postProcessing/blur.frag");
    }

    protected void loadTargetWidth(float width) {
        super.loadFloatToUniform(location_targetWidth, width);
    }

    @Override
    protected void getAllUniformLocations() {
        location_targetWidth = super.getUniformLocation("targetWidth");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
