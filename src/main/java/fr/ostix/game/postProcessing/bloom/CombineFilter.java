package fr.ostix.game.postProcessing.bloom;

import fr.ostix.game.postProcessing.ImageRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


public class CombineFilter {

	private final ImageRenderer renderer;
	private final CombineShader shader;

	public CombineFilter() {
		shader = new CombineShader();
		shader.bind();
		shader.connectTextureUnits();
		shader.unBind();
		renderer = new ImageRenderer();
	}

	public void render(int colourTexture, int highlightTexture) {
		shader.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);
		renderer.renderQuad();
		shader.bind();
	}

	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

}
