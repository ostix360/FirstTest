package fr.ostix.game.graphics.model;

public class MeshModel {

    private int vaoID;
    private int vertexCount;

    public MeshModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
