package fr.ostix.game.postProcessing.blur;

import fr.ostix.game.postProcessing.ImageRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


public class VerticalBlur {

    private final ImageRenderer renderer;
    private final VerticalBlurShader shader;

    public VerticalBlur(int targetFboWidth, int targetFboHeight) {
        shader = new VerticalBlurShader();
        renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
        shader.bind();
        shader.loadTargetHeight(targetFboHeight);
    }


    public void render(int texture) {
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
    }

    public int getOutputTexture() {
        return renderer.getOutputTexture();
    }

    public void cleanUp() {
        renderer.cleanUp();
        shader.cleanUp();
    }
}
