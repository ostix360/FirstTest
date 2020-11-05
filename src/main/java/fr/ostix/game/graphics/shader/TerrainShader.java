package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.entities.Light;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class TerrainShader extends ShaderProgram{

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;


    public TerrainShader() {
        super("terrainShader.vert", "terrainShader.frag");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("shineDamper");
        location_skyColour = super.getUniformLocation("skyColour");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
    }

    public void connectTerrainUnits(){
        super.loadInt(location_backgroundTexture,0);
        super.loadInt(location_rTexture,1);
        super.loadInt(location_gTexture,2);
        super.loadInt(location_bTexture,3);
        super.loadInt(location_blendMap,4);

    }

    public void loadSkyColour(Color colour){
        super.loadVerctorToUniform(location_skyColour,colour.getVec3f());
    }

    public void loadShineVariables(float damper,float reflectivity){
        super.loadFloatToUniform(location_shineDamper,damper);
        super.loadFloatToUniform(location_reflectivity,reflectivity);
    }

    public void loadLight(Light light){
        super.loadVerctorToUniform(location_lightPosition, light.getPosition());
        super.loadVerctorToUniform(location_lightColour,light.getColourVec3f());
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrixToUniform(location_transformationMatrix,matrix);
    }

    public void loadViewMatrix(Camera cam){
        Matrix4f viewMatrix = Maths.createViewMatrix(cam);
        super.loadMatrixToUniform(location_viewMatrix,viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrixToUniform(location_projectionMatrix,projection);
    }
}
