package fr.ostix.game.postProcessing.bloom;


import fr.ostix.game.graphics.shader.ShaderProgram;

public class BrightFilterShader extends ShaderProgram {


	public BrightFilterShader() {
		super("postProcessing/simple.vert", "postProcessing/brightFilter.frag");
	}

	@Override
	protected void getAllUniformLocations() {
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
