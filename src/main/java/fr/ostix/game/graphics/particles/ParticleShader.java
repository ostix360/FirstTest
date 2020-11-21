package fr.ostix.game.graphics.particles;

import fr.ostix.game.graphics.shader.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class ParticleShader extends ShaderProgram {


	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	private int location_offsets1;
	private int location_offsets2;
	private int location_texCoordsInfo;

	public ParticleShader() {
		super("particle.vert", "particle.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_offsets1 = super.getUniformLocation("offsets1");
		location_offsets2 = super.getUniformLocation("offsets2");
		location_texCoordsInfo = super.getUniformLocation("texCoordsInfo");

	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadTextureCoordsInfo(Vector2f offsets1, Vector2f offsets2, float numberOfRow, float blend) {
		super.loadVerctor2fToUniform(location_offsets1, offsets1);
		super.loadVerctor2fToUniform(location_offsets2, offsets2);
		super.loadVerctor2fToUniform(location_texCoordsInfo, new Vector2f(numberOfRow, blend));

	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrixToUniform(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrixToUniform(location_projectionMatrix, projectionMatrix);
	}


}
