package fr.ostix.game.gui;

import fr.ostix.game.graphics.textures.ModelTexture;
import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
    private final int textureID;
    private Vector2f position;
    private Vector2f scale;
    private int textureIndex;
    private ModelTexture texture;

    public GUITexture(ModelTexture texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.textureID = texture.geID();
        this.position = position;
        this.scale = scale;
    }

    public GUITexture(int texture, Vector2f position, Vector2f scale) {
        this.textureID = texture;
        this.position = position;
        this.scale = scale;
    }

    public GUITexture(ModelTexture texture, int textureIndex, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.textureID = texture.geID();
        this.position = position;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public float getTextureXOffset() {
        float column = textureIndex % texture.getNumbersOfRows();
        return column / texture.getNumbersOfRows();
    }

    public float getTextureYoffset() {
        float row = textureIndex / (float) texture.getNumbersOfRows();
        return row / texture.getNumbersOfRows();
    }

    public int getTextureID() {
        return textureID;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void setTextureIndex(int index) {
        this.textureIndex = index;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
