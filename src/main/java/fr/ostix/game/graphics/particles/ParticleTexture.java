package fr.ostix.game.graphics.particles;

import fr.ostix.game.graphics.textures.Texture;

public class ParticleTexture {

    private final Texture texture;
    private final int numberOfRows;
    private final boolean additive;

    public ParticleTexture(Texture texture, int numberOfRows, boolean additive) {
        this.additive = additive;
        this.texture = texture;
        this.numberOfRows = numberOfRows;
    }

    public boolean isAdditive() {
        return additive;
    }

    public int getTextureID() {
        return texture.getId();
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
