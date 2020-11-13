package fr.ostix.game.font.rendering;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.font.meshCreator.FontType;
import fr.ostix.game.font.meshCreator.GUIText;
import fr.ostix.game.font.meshCreator.TextMeshData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterFont {

    private static final Map<FontType, List<GUIText>> guisTexts = new HashMap<>();
    private static Loader loader;
    private static FontRenderer renderer;

    public static void init(Loader theLoader) {
        renderer = new FontRenderer();
        loader = theLoader;
    }

    public static void render() {
        renderer.render(guisTexts);
    }

    public static void loadTexte(GUIText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textBatch = guisTexts.get(font);
        if (textBatch == null) {
            textBatch = new ArrayList<>();
            guisTexts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    public static void remove(GUIText text) {
        List<GUIText> texts = guisTexts.get(text.getFont());
        texts.remove(text);
        if (texts.isEmpty()) {
            guisTexts.remove(text.getFont());
        }

    }

    public static void cleanUp() {
        renderer.cleanUp();
    }
}
