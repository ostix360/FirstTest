package fr.ostix.game.font.rendering;


import fr.ostix.game.font.meshCreator.FontType;
import fr.ostix.game.font.meshCreator.GUIText;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {

    private final FontShader shader;

    public FontRenderer() {
        shader = new FontShader();
    }

    public void render(Map<FontType, List<GUIText>> guisTexts) {
        prepare();
        for (FontType font : guisTexts.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
            for (GUIText text : guisTexts.get(font)) {
                renderText(text);
            }
        }
        unBind();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

    private void prepare() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        shader.bind();
    }

    private void renderText(GUIText text) {
        GL30.glBindVertexArray(text.getVao());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadColor(text.getColour());
        shader.loadTranslation(text.getPosition());
        glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);


    }

    private void unBind() {
        shader.unBind();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

}
