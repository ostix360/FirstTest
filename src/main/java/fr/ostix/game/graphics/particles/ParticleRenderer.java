package fr.ostix.game.graphics.particles;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.math.Maths;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ParticleRenderer {

	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

	private final MeshModel quad;
	private final ParticleShader shader;

	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		quad = loader.loadToVAO(VERTICES, 2);
		shader = new ParticleShader();
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.unBind();
	}

	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		for (ParticleTexture texture : particles.keySet()) {
			if (texture.isAdditive()) {
				glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			} else {
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
			for (Particle p : particles.get(texture)) {
				updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix);
				shader.loadTextureCoordsInfo(p.getOffsets1(), p.getOffsets2(), texture.getNumberOfRows(), p.getBlend());
				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		finish();
	}

	private void prepare() {
		glEnable(GL_BLEND);
		glDepthMask(false);
		shader.bind();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(position);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(scale, scale, scale));
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		shader.loadModelViewMatrix(modelViewMatrix);
	}

	private void finish() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.unBind();
	}

	protected void cleanUp() {
		shader.cleanUp();
	}

}
