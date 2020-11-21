package fr.ostix.game.graphics.particles;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.math.Maths;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ParticleRenderer {

    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final int MAX_INSTANCES = 10000;
    private static final int INSTANCE_DATA_LENGTH = 21;

    public static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);

    private final MeshModel quad;
    private final ParticleShader shader;

    private final Loader loader;
    private final int vboID;
    private int pointer = 0;

    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
        this.loader = loader;
        this.vboID = loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);
        loader.addInstance(quad.getVaoID(), this.vboID, 1, 4, INSTANCE_DATA_LENGTH, 0);
        loader.addInstance(quad.getVaoID(), this.vboID, 2, 4, INSTANCE_DATA_LENGTH, 4);
        loader.addInstance(quad.getVaoID(), this.vboID, 3, 4, INSTANCE_DATA_LENGTH, 8);
        loader.addInstance(quad.getVaoID(), this.vboID, 4, 4, INSTANCE_DATA_LENGTH, 12);
        loader.addInstance(quad.getVaoID(), this.vboID, 5, 4, INSTANCE_DATA_LENGTH, 16);
        loader.addInstance(quad.getVaoID(), this.vboID, 6, 1, INSTANCE_DATA_LENGTH, 20);
        shader = new ParticleShader();
        shader.bind();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unBind();
    }

    protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();
        for (ParticleTexture texture : particles.keySet()) {
            bindTexture(texture);
            List<Particle> particlesList = particles.get(texture);
            pointer = 0;
            float[] vboData = new float[particlesList.size() * INSTANCE_DATA_LENGTH];
            for (Particle p : particlesList) {
                updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix, vboData);
                updateTexCoordsInfo(p, vboData);
            }
            loader.updateVBO(vboID, vboData, BUFFER);
            GL32.glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particlesList.size());
        }
        finish();
    }

    private void prepare() {
        glEnable(GL_BLEND);
        glDepthMask(false);
        shader.bind();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);

    }

    private void bindTexture(ParticleTexture texture) {
        if (texture.isAdditive()) {
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        } else {
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.loadNumberOfRows(texture.getNumberOfRows());
    }

    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.translate(position);
        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;
        modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
        modelMatrix.scale(new Vector3f(scale, scale, scale));
        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        storeMatrixData(modelViewMatrix, vboData);
    }

    private void storeMatrixData(Matrix4f matrix, float[] vboData) {
        vboData[pointer++] = matrix.m00;
        vboData[pointer++] = matrix.m01;
        vboData[pointer++] = matrix.m02;
        vboData[pointer++] = matrix.m03;
        vboData[pointer++] = matrix.m10;
        vboData[pointer++] = matrix.m11;
        vboData[pointer++] = matrix.m12;
        vboData[pointer++] = matrix.m13;
        vboData[pointer++] = matrix.m20;
        vboData[pointer++] = matrix.m21;
        vboData[pointer++] = matrix.m22;
        vboData[pointer++] = matrix.m23;
        vboData[pointer++] = matrix.m30;
        vboData[pointer++] = matrix.m31;
        vboData[pointer++] = matrix.m32;
        vboData[pointer++] = matrix.m33;
    }

    private void updateTexCoordsInfo(Particle p, float[] data) {
        data[pointer++] = p.getOffsets1().getX();
        data[pointer++] = p.getOffsets1().getY();
        data[pointer++] = p.getOffsets2().getX();
        data[pointer++] = p.getOffsets2().getY();
        data[pointer++] = p.getBlend();
    }

    private void finish() {
        glDepthMask(true);
        glDisable(GL_BLEND);
        GL20.glDisableVertexAttribArray(6);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unBind();
    }

    protected void cleanUp() {
        shader.cleanUp();
    }

}
