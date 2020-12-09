package fr.ostix.game.graphics.model.normalMappingRenderer;


import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Entity;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.render.MasterRenderer;
import fr.ostix.game.graphics.textures.ModelTexture;
import fr.ostix.game.math.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;


public class NormalMappingRenderer {

	private final NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.bind();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.unBind();
	}

	public void render(Map<TextureModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.bind();
		prepare(clipPlane, lights, camera);
		for (TextureModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMeshModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.unBind();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepareTexturedModel(TextureModel model) {
		MeshModel rawModel = model.getMeshModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		ModelTexture texture = model.getModelTexture();
		shader.loadNumberOfRows(texture.getNumbersOfRows());
		if (texture.isTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().geID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getNormalMapID());
		shader.loadUseSpecularMap(texture.hasSpecularMap());
		if (texture.hasSpecularMap()){
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, texture.getSpecularMap());
		}
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.loadClipPlane(clipPlane);
		//need to be public variables in MasterRenderer
		shader.loadSkyColour(MasterRenderer.skyColor);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);

		shader.loadLights(lights, viewMatrix);
		shader.loadViewMatrix(viewMatrix);
	}

}
