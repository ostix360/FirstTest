package fr.ostix.game.postProcessing;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.graphics.model.MeshModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class PostProcessing {

	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static MeshModel quad;
	private static ContrastChanger contrastChanger;

	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
	}

	public static void doPostProcessing(int colourTexture) {
		start();
		contrastChanger.applyContrast(colourTexture);
		end();
	}

	public static void cleanUp() {
		contrastChanger.cleanUp();
	}

	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
