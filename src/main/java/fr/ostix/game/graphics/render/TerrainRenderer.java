package fr.ostix.game.graphics.render;

import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.shader.TerrainShader;
import fr.ostix.game.math.Maths;
import fr.ostix.game.world.Terrain;
import fr.ostix.game.world.texture.TerrainTexturePack;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class TerrainRenderer {
    private final TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.bind();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTerrainUnits();
        shader.unBind();
    }

    public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {
        shader.loadShaderMapSpace(toShadowSpace);
        for (Terrain ter : terrains) {
            prepareTerrain(ter);
            loadModelMatrix(ter);
            glDrawElements(GL_TRIANGLES, ter.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        MeshModel model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.loadShineVariables(1, 0);
        bindTexture(terrain);
    }

    private void bindTexture(Terrain ter){
        TerrainTexturePack texturePack = ter.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, ter.getBlendMap().getTextureID());
    }

    private void unbindTexturedModel(){
        glBindTexture(GL_TEXTURE_2D,0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glDeleteVertexArrays(0);
    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()),
                0,0,0,1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
