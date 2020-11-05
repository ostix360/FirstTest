package fr.ostix.game.gui;

import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
    private final int textureID;
    private final Vector2f position;
    private final Vector2f scale;

    public GUITexture(int textureID, Vector2f position, Vector2f scale) {
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
