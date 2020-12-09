package fr.ostix.game.graphics.render;

import fr.ostix.game.core.DisplayManager;
import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Entity;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.model.normalMappingRenderer.NormalMappingRenderer;
import fr.ostix.game.graphics.shader.Shader;
import fr.ostix.game.graphics.shader.TerrainShader;
import fr.ostix.game.graphics.shadows.ShadowMapMasterRenderer;
import fr.ostix.game.skybox.SkyboxRenderer;
import fr.ostix.game.world.Terrain;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static Color skyColor;

    private Matrix4f projectionMatrix;

    private final EntityRenderer entityRenderer;
    private final Shader shader = new Shader();


    private final TerrainRenderer terrainRenderer;
    private final TerrainShader terrainShader = new TerrainShader();

    private final NormalMappingRenderer normalMappingRenderer;

    private final SkyboxRenderer skyboxRenderer;
    private final ShadowMapMasterRenderer shadowRenderer;

    private final Map<TextureModel, List<Entity>> entities = new HashMap<>();
    private final Map<TextureModel, List<Entity>> normalMapEntities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(Loader loader, Camera cam) {
        enableCulling();
        createProjectionMatrix();
        this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        this.shadowRenderer = new ShadowMapMasterRenderer(cam);
        this.normalMappingRenderer = new NormalMappingRenderer(projectionMatrix);
    }

    public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, Terrain[][] terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
        for (Terrain[] t : terrains) {
            for (Terrain tn : t) {
                processTerrain(tn);
            }
        }
        for (Entity entity : entities) {
            processEntity(entity);
        }
        for (Entity e : normalMapEntities) {
            processNormalMapEntity(e);
        }
        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera cam, Vector4f clipPlane) {
        this.initFrame(new Color(0.5444f, 0.62f, 0.69f));
        shader.bind();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(skyColor);
        shader.loadLights(lights);
        shader.loadViewMatrix(cam);
        entityRenderer.render(entities, shadowRenderer.getToShadowMapSpaceMatrix());
        shader.unBind();
        normalMappingRenderer.render(normalMapEntities, clipPlane, lights, cam);
        terrainShader.bind();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(skyColor);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(cam);
        terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
        skyboxRenderer.render(cam, skyColor);
        terrains.clear();
        entities.clear();
        normalMapEntities.clear();
    }

    public void initFrame(Color color) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, shadowRenderer.getShadowMap());
        skyColor = color;
    }

    public void renderShadowMap(List<Entity> entities, Light sun) {
        for (Entity e : entities) {
            processEntity(e);
        }
        shadowRenderer.render(this.entities, sun);
        this.entities.clear();
    }

    public int getShadowMapTexture() {
        return this.shadowRenderer.getShadowMap();
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    private void processEntity(Entity e) {
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

    private void processNormalMapEntity(Entity e) {
        TextureModel model = e.getModel();
        List<Entity> batch = normalMapEntities.get(model);
        if (batch != null) {
            batch.add(e);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(e);
            normalMapEntities.put(model, newBatch);
        }
    }

    private void processTerrain(Terrain t) {
        terrains.add(t);
    }


    public void cleanUp() {
        this.normalMappingRenderer.cleanUp();
        this.shader.cleanUp();
        this.terrainShader.cleanUp();
        glDisable(GL_BLEND);
        this.shadowRenderer.cleanUp();
    }


    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
