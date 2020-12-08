package fr.ostix.game.postProcessing.blur;

import fr.ostix.game.postProcessing.ImageRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class HorizontalBlur {

    private final ImageRenderer renderer;
    private final HorizontalBlurShader shader;

    public HorizontalBlur(int targetFboWidth, int targetFboHeight) {
        shader = new HorizontalBlurShader();
        shader.bind();
        shader.loadTargetWidth(targetFboWidth);
        shader.unBind();
        renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
    }

    public void render(int texture) {
        shader.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.unBind();
    }

    public int getOutputTexture() {
        return renderer.getOutputTexture();
    }

    public void cleanUp() {
        renderer.cleanUp();
        shader.cleanUp();
    }

}
