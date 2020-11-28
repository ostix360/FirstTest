package fr.ostix.game.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {

    private final ImageRenderer renderer;
    private final ContrastShader shader;

    public ContrastChanger() {
        renderer = new ImageRenderer();
        shader = new ContrastShader();
    }

    public void applyContrast(int texture) {
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.unBind();
    }

    public void cleanUp() {
        renderer.cleanUp();
        shader.cleanUp();
    }
}
