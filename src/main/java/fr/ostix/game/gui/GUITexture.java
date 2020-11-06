package fr.ostix.game.gui;

import fr.ostix.game.graphics.textures.ModelTexture;
import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
    private final ModelTexture texture;
    private Vector2f position;
    private Vector2f scale;
    private int textureIndex;

    public GUITexture(ModelTexture texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public GUITexture(ModelTexture texture, int textureIndex, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public float getTextureXOffset() {
        float column = textureIndex % texture.getNubersOfRows();
        return column / texture.getNubersOfRows();
    }

    public float getTextureYoffset() {
        float row = textureIndex / (float) texture.getNubersOfRows();
        return row / texture.getNubersOfRows();
    }

    public int getTextureID() {
        return texture.geID();
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
