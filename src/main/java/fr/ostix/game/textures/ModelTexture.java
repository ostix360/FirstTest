package fr.ostix.game.textures;


public class ModelTexture {
    private final int textureID;

    private float shineDamper = 0;
    private float reflectivity = 0;

    private int nubersOfRows = 1;

    private boolean isTransparency = false;
    private boolean useFakeLighting = false;

    public ModelTexture(Texture texture) {
        this.textureID = texture.getId();
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public ModelTexture setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
        return this;
    }

    public boolean isTransparency() {
        return isTransparency;
    }

    public ModelTexture setTransparency(boolean transparency) {
        isTransparency = transparency;
        return this;
    }

    public int getNubersOfRows() {
        return nubersOfRows;
    }

    public int geID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }


}
