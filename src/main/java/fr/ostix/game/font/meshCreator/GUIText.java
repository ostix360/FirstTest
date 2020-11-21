package fr.ostix.game.font.meshCreator;

import fr.ostix.game.font.rendering.MasterFont;
import fr.ostix.game.graphics.Color;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


public class GUIText {

    private final String textString;
    private final float fontSize;
    private final boolean centerText;
    private final Vector2f position;
    private final float lineMaxSize;
    private final FontType font;
    private int textMeshVao;
    private int vertexCount;
    private int numberOfLines;
    private Vector3f colour = new Vector3f(0f, 0f, 0f);

    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
                   boolean centered) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        MasterFont.loadTexte(this);
    }

    public void remove() {
        MasterFont.remove(this);
    }

    public FontType getFont() {
        return font;
    }

    public void setColour(Color c) {
        colour = c.getVec3f();
    }


    public Vector3f getColour() {
        return colour;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getVao() {
        return textMeshVao;
    }

    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    protected float getFontSize() {
        return fontSize;
    }

    protected boolean isCentered() {
        return centerText;
    }

    protected float getMaxLineSize() {
        return lineMaxSize;
    }

    protected String getTextString() {
        return textString;
    }

}
