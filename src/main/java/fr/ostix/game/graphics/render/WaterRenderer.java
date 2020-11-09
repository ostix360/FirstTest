package fr.ostix.game.graphics.render;

import fr.ostix.game.core.DisplayManager;
import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.shader.WaterShader;
import fr.ostix.game.math.Maths;
import fr.ostix.game.world.water.WaterFrameBuffers;
import fr.ostix.game.world.water.WaterTile;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class WaterRenderer {

    private static final String DUDV_MAP = "water/waterDUDV";
    private static final float WAVE_SPEED = 0.6f;

    private final WaterShader shader;
    private final WaterFrameBuffers fbos;
    private MeshModel quad;

    private final int dudvTexture;
    private float waveMove = 0;

    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.fbos = fbos;
        this.shader = shader;
        this.dudvTexture = loader.loadTexture(DUDV_MAP).getId();
        shader.bind();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unBind();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, Camera camera) {
        prepareRender(camera);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(Camera camera) {
        shader.bind();
        shader.loadViewMatrix(camera);
        waveMove += WAVE_SPEED * DisplayManager.getFrameTimeSeconde();
        waveMove %= 1;
        shader.loadWaveFactor(waveMove);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, dudvTexture);
    }

    private void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unBind();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
        quad = loader.loadToVAO(vertices, 2);
    }

    public void cleanUp() {
        shader.cleanUP();
    }
}
