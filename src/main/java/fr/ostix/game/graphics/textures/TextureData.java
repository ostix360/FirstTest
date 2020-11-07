package fr.ostix.game.graphics.textures;

import java.nio.ByteBuffer;

public class TextureData {
    private final int wigth;
    private final int height;
    private final ByteBuffer buffer;

    public TextureData(int wigth, int height, ByteBuffer buffer) {
        this.wigth = wigth;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWigth() {
        return wigth;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
