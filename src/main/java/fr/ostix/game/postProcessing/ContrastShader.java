package fr.ostix.game.postProcessing;


import fr.ostix.game.graphics.shader.ShaderProgram;

public class ContrastShader extends ShaderProgram {


	public ContrastShader() {
		super("postProcessing/contrast.vert", "postProcessing/contrast.frag");
	}

	@Override
	protected void getAllUniformLocations() {
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
