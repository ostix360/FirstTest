package fr.ostix.game.graphics.particles;

import fr.ostix.game.graphics.shader.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {


	private int location_projectionMatrix;
	private int location_numberOfRows;

	public ParticleShader() {
		super("particle.vert", "particle.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_numberOfRows = super.getUniformLocation("numberOfRows");

	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");

	}

	protected void loadNumberOfRows(float numberOfRows) {
		super.loadFloatToUniform(location_numberOfRows, numberOfRows);
	}

	protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrixToUniform(location_projectionMatrix, projection);
	}

}
