package fr.ostix.game.entities;

import fr.ostix.game.graphics.model.TextureModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    protected TextureModel model;
    protected Vector3f position;
    protected float rotx, roty, rotz;
    protected float scale;

    private int textureIndex = 0;

    public Entity(TextureModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {
        this.model = model;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.scale = scale;
    }

    public Entity(TextureModel model,int textureIndex, Vector3f position, float rotx, float roty, float rotz, float scale) {
        this.model = model;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public void increasePosition(float dx,float dy,float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float rx, float ry, float rz) {
        this.rotx += rx;
        this.roty += ry;
        this.rotz += rz;
    }

    public float getTextureXOffset() {
        float column = textureIndex % model.getModelTexture().getNumbersOfRows();
        return column / model.getModelTexture().getNumbersOfRows();
    }

    public float getTextureYOffset() {
        float row = textureIndex / (float) model.getModelTexture().getNumbersOfRows();
        return row / model.getModelTexture().getNumbersOfRows();
    }

    public TextureModel getModel() {
        return model;
    }

    public void setModel(TextureModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return new Vector3f(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotx;
    }

    public void setRotx(float rotx) {
        this.rotx = rotx;
    }

    public float getRotY() {
        return roty;
    }

    public void setRoty(float roty) {
        this.roty = roty;
    }

    public float getRotZ() {
        return rotz;
    }

    public void setRotz(float rotz) {
        this.rotz = rotz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
