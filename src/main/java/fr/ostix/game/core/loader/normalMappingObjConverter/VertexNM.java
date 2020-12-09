package fr.ostix.game.core.loader.normalMappingObjConverter;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class VertexNM {

	private static final int NO_INDEX = -1;

	private final Vector3f position;
	private final int index;
	private final float length;
	private final Vector3f averagedTangent = new Vector3f(0, 0, 0);
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private VertexNM duplicateVertex = null;
	private List<Vector3f> tangents = new ArrayList<>();

	protected VertexNM(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.length();
	}

	protected void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}

	//NEW
	protected VertexNM duplicate(int newIndex) {
		VertexNM vertex = new VertexNM(newIndex, position);
		vertex.tangents = this.tangents;
		return vertex;
	}

	protected void averageTangents() {
		if (tangents.isEmpty()) {
			return;
		}
		for (Vector3f tangent : tangents) {
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}

	protected Vector3f getAverageTangent() {
		return averagedTangent;
	}

	protected int getIndex() {
		return index;
	}

	protected float getLength() {
		return length;
	}

	protected boolean isSet() {
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}

	protected boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}

	protected Vector3f getPosition() {
		return position;
	}

	protected int getTextureIndex() {
		return textureIndex;
	}

	protected void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	protected int getNormalIndex() {
		return normalIndex;
	}

	protected void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	protected VertexNM getDuplicateVertex() {
		return duplicateVertex;
	}

	protected void setDuplicateVertex(VertexNM duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
