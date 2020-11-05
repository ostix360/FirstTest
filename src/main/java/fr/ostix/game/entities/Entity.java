package fr.ostix.game.entities;

import fr.ostix.game.graphics.model.TextureModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TextureModel model;
    private Vector3f position;
    private float rotx,roty,rotz;
    private float scale;

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

    public void increaseRotation(float rx,float ry,float rz){
        this.rotx += rx;
        this.roty += ry;
        this.rotz += rz;
    }

    public float getTextureXOffset(){
        float column = textureIndex%model.getModelTexture().getNubersOfRows();
        return column/model.getModelTexture().getNubersOfRows();
    }

    public float getTextureYoffset(){
        float row = textureIndex/(float)model.getModelTexture().getNubersOfRows();
        return row/model.getModelTexture().getNubersOfRows();
    }

    public TextureModel getModel() {
        return model;
    }

    public void setModel(TextureModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotx() {
        return rotx;
    }

    public void setRotx(float rotx) {
        this.rotx = rotx;
    }

    public float getRoty() {
        return roty;
    }

    public void setRoty(float roty) {
        this.roty = roty;
    }

    public float getRotz() {
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
