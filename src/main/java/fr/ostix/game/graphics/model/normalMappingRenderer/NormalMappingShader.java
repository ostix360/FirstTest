package fr.ostix.game.graphics.model.normalMappingRenderer;

import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.graphics.shader.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;


public class NormalMappingShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int[] location_lightPositionEyeSpace;
	private int[] location_lightColour;
	private int[] location_attenuation;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_modelTexture;
	private int location_normalMapTexture;

	public NormalMappingShader() {
		super("normalMap.vert", "normalMap.frag");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_normalMapTexture = super.getUniformLocation("normalMapTexture");

		location_lightPositionEyeSpace = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	protected void connectTextureUnits() {
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_normalMapTexture, 1);
	}

	protected void loadClipPlane(Vector4f plane) {
		super.loadVerctor4fToUniform(location_plane, plane);
	}

	protected void loadNumberOfRows(int numberOfRows) {
		super.loadFloatToUniform(location_numberOfRows, numberOfRows);
	}

	protected void loadOffset(float x, float y) {
		super.loadVerctor2fToUniform(location_offset, new Vector2f(x, y));
	}

	protected void loadSkyColour(Color color) {
		super.loadVerctor3fToUniform(location_skyColour, color.getVec3f());
	}

	protected void loadShineVariables(float damper, float reflectivity) {
		super.loadFloatToUniform(location_shineDamper, damper);
		super.loadFloatToUniform(location_reflectivity, reflectivity);
	}

	protected void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrixToUniform(location_transformationMatrix, matrix);
	}

	protected void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVerctor3fToUniform(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVerctor3fToUniform(location_lightColour[i], lights.get(i).getColourVec3f());
				super.loadVerctor3fToUniform(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVerctor3fToUniform(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
				super.loadVerctor3fToUniform(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVerctor3fToUniform(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	protected void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrixToUniform(location_viewMatrix, viewMatrix);
	}

	protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrixToUniform(location_projectionMatrix, projection);
	}

	private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}


}
