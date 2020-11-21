package fr.ostix.game.graphics.render;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.shader.GUIShader;
import fr.ostix.game.gui.GUITexture;
import fr.ostix.game.math.Maths;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GUIRenderer {

    private final MeshModel quadModel;
    private final GUIShader shader;

    public GUIRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quadModel = loader.loadToVAO(positions, 2);
        shader = new GUIShader();
    }

    public void render(List<GUITexture> guis) {
        shader.bind();
        GL30.glBindVertexArray(quadModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        for (GUITexture gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, gui.getTextureID());
            Matrix4f matrix4f = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformation(matrix4f);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, quadModel.getVertexCount());
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unBind();
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}
