package fr.ostix.game.graphics.model;

import fr.ostix.game.graphics.textures.ModelTexture;

public class TextureModel {

    private final MeshModel meshModel;
    private final ModelTexture modelTexture;

    public TextureModel(MeshModel meshModel, ModelTexture modelTexture) {
        this.meshModel = meshModel;
        this.modelTexture = modelTexture;
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
