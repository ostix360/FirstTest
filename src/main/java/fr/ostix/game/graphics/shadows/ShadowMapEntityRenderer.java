package fr.ostix.game.graphics.shadows;

import fr.ostix.game.entities.Entity;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.render.MasterRenderer;
import fr.ostix.game.math.Maths;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;


public class ShadowMapEntityRenderer {

	private final Matrix4f projectionViewMatrix;
	private final ShadowShader shader;

	/**
	 * @param shader               - the simple shader program being used for the shadow render
	 *                             pass.
	 * @param projectionViewMatrix - the orthographic projection matrix multiplied by the light's
	 *                             "view" matrix.
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders entieis to the shadow map. Each model is first bound and then all
	 * of the entities using that model are rendered to the shadow map.
	 *
	 * @param entities - the entities to be rendered to the shadow map.
	 */
	protected void render(Map<TextureModel, List<Entity>> entities) {
		for (TextureModel model : entities.keySet()) {
			MeshModel meshModel = model.getMeshModel();
			bindModel(meshModel);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, model.getModelTexture().geID());
			if (model.getModelTexture().isTransparency()) {
				MasterRenderer.disableCulling();
			}
			for (Entity entity : entities.get(model)) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, meshModel.getVertexCount(),
						GL_UNSIGNED_INT, 0);
			}
			if (model.getModelTexture().isTransparency()) {
				MasterRenderer.enableCulling();
			}
		}
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Binds a raw model before rendering. Only the attribute 0 is enabled here
	 * because that is where the positions are stored in the VAO, and only the
	 * positions are required in the vertex shader.
	 *
	 * @param rawModel - the model to be bound.
	 */
	private void bindModel(MeshModel rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}

	/**
	 * Prepares an entity to be rendered. The model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 *
	 * @param entity - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
