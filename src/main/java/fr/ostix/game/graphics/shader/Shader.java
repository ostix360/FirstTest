package fr.ostix.game.graphics.shader;

import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.entities.Light;
import fr.ostix.game.math.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class Shader extends ShaderProgram{

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;


    public Shader() {
        super("defaultShader.vert", "defaultShader.frag");
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
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColour = super.getUniformLocation("skyColour");
    }

    public void loadSkyColour(Color colour){
        super.loadVerctorToUniform(location_skyColour,colour.getVec3f());
    }

    public void loadFakeLightingVariable(boolean isFakeLighting){
        super.loadBooleanToUniform(location_useFakeLighting,isFakeLighting);
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
