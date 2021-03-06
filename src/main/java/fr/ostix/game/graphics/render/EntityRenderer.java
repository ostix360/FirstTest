package fr.ostix.game.graphics.render;

import fr.ostix.game.entities.Entity;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.shader.Shader;
import fr.ostix.game.graphics.textures.ModelTexture;
import fr.ostix.game.math.Maths;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class EntityRenderer {


    private final Shader shader;

    public EntityRenderer(Shader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.bind();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.unBind();
    }


    public void render(Map<TextureModel, List<Entity>> entities, Matrix4f toShadowSpace) {
        shader.loadShaderMapSpace(toShadowSpace);
        for (TextureModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                glDrawElements(GL_TRIANGLES, model.getMeshModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TextureModel textureModel) {
        MeshModel model = textureModel.getMeshModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = textureModel.getModelTexture();
        shader.loadNumbuerOfRows(texture.getNumbersOfRows());
        if (texture.isTransparency())MasterRenderer.disableCulling();
        shader.loadFakeLightingVariable(texture.useFakeLighting());
        shader.loadInverseNormal(texture.isInverseNormal());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,texture.geID());
        shader.loadUseSpecularMap(texture.hasSpecularMap());
        if (texture.hasSpecularMap()){
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture.getSpecularMap());
        }
    }

    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        glBindTexture(GL_TEXTURE_2D,0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glDeleteVertexArrays(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(),
                entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
}