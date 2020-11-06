package fr.ostix.game.graphics.render;

import fr.ostix.game.core.DisplayManager;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Entity;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.shader.Shader;
import fr.ostix.game.graphics.shader.TerrainShader;
import fr.ostix.game.world.Terrain;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
    private static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    private Color skyColor;

    private Matrix4f projectionMatrix;

    private final EntityRenderer entityRenderer;
    private final Shader shader = new Shader();


    private final TerrainRenderer terrainRenderer;
    private final TerrainShader terrainShader = new TerrainShader();

    private final Map<TextureModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer() {
        enableCulling();
        createProjectionMatirx();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(List<Light> ligths, Camera cam) {
        this.initFrame(Color.GRAY);
        shader.bind();
        shader.loadSkyColour(skyColor);
        shader.loadLights(ligths);
        shader.loadViewMatrix(cam);
        entityRenderer.render(entities);
        shader.unBind();
        terrainShader.bind();
        terrainShader.loadSkyColour(skyColor);
        terrainShader.loadLights(ligths);
        terrainShader.loadViewMatrix(cam);
        terrainRenderer.render(terrains);
        terrains.clear();
        entities.clear();
    }

    public void initFrame(Color color) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        this.skyColor = color;
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private void createProjectionMatirx() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void processEntity(Entity e) {
        TextureModel model = e.getModel();
        List<Entity> batch = entities.get(model);
        if (batch != null) {
            batch.add(e);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(e);
            entities.put(model, newBatch);
        }
    }

    public void processTerrain(Terrain t) {
        terrains.add(t);
    }


    public void cleanUp() {
        shader.cleanUP();
        terrainShader.cleanUP();
    }


}
