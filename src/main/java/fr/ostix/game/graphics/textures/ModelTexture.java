package fr.ostix.game.graphics.textures;


public class ModelTexture {
    private final int textureID;
    private int normalMapID;

    private float shineDamper = 0;
    private float reflectivity = 0;

    private int numbersOfRows = 1;

    private boolean isTransparency = false;
    private boolean useFakeLighting = false;
    private boolean isInverseNormal = false;

    public ModelTexture(Texture texture) {
        this.textureID = texture.getId();
    }

    public ModelTexture(int texture) {
        this.textureID = texture;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public ModelTexture setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
        return this;
    }

    public boolean isInverseNormal() {
        return isInverseNormal;
    }

    public ModelTexture setInverseNormal(boolean value) {
        this.isInverseNormal = value;
        return this;
    }

    public boolean isTransparency() {
        return isTransparency;
    }

    public ModelTexture setTransparency(boolean transparency) {
        isTransparency = transparency;
        return this;
    }

    public ModelTexture setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
        return this;
    }

    public ModelTexture setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
        return this;
    }

    public int getNormalMapID() {
        return normalMapID;
    }

    public void setNormalMapID(int normalMapID) {
        this.normalMapID = normalMapID;
    }

    public int getNumbersOfRows() {
        return numbersOfRows;
    }

    public void setNumbersOfRows(int numbersOfRows) {
        this.numbersOfRows = numbersOfRows;
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
