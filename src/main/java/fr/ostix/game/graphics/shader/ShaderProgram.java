package fr.ostix.game.graphics.shader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {

    private static final Logger LOGGER = LogManager.getLogger(ShaderProgram.class);

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShader, String fragmentShader) {
        this.vertexShaderID = loadShader(vertexShader, GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentShader, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID,vertexShaderID);
        glAttachShader(programID,fragmentShaderID);

        bindAttributes();

        glLinkProgram(programID);
        if (glGetProgrami(programID,GL_LINK_STATUS) == GL_FALSE){
            LOGGER.error("Failed to linked shader program : " + glGetProgramInfoLog(programID),
                    new IllegalStateException("Could not compile shader"));
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID,GL_VALIDATE_STATUS) == GL_FALSE){
            LOGGER.error("Failed to validate shader program : " + glGetProgramInfoLog(programID),
                    new IllegalStateException("Could not compile shader"));
        }
        getAllUniformLocations();
    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return glGetUniformLocation(programID,uniformName);
    }

    protected void bindAttribute(int attribut,String variableName){
        glBindAttribLocation(programID,attribut,variableName);
    }

    protected void loadInt(int location,int value){
        glUniform1i(location,value);
    }

    protected void loadFloatToUniform(int location,float value){
        glUniform1f(location,value);
    }
    protected void loadVerctor2fToUniform(int location, Vector2f vector){
        glUniform2f(location,vector.x,vector.y);
    }

    protected void loadVerctor3fToUniform(int location, Vector3f vector){
        glUniform3f(location,vector.x,vector.y,vector.z);
    }

    protected void loadBooleanToUniform(int location,boolean value){
        float fValue = 0;
        if (value){
            fValue = 1;
        }
        glUniform1f(location,fValue);
    }

    protected void loadMatrixToUniform(int location, Matrix4f matrix){
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(location,false,matrixBuffer);
    }

    public void bind(){
        glUseProgram(programID);
    }

    public void unBind(){
        glUseProgram(0);
    }


    public void cleanUP(){
        unBind();
        glDetachShader(programID,vertexShaderID);
        glDetachShader(programID,fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }


    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = readShader(file);
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            LOGGER.error("Failed to compile shader : " + file + " || GL error : " + glGetShaderInfoLog(shaderID),
                    new IllegalStateException("Could not compile shader"));
            System.exit(1);
        }
        return shaderID;
    }

    private static StringBuilder readShader(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(ShaderProgram.class.getResource("/shaders/" + file).toURI())));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Could not read file ", e);
        }
        return sb;
    }


}
