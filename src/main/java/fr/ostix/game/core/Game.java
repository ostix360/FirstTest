package fr.ostix.game.core;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.core.loader.OBJFileLoader;
import fr.ostix.game.core.loader.normalMappingObjConverter.NormalMappedObjLoader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Entity;
import fr.ostix.game.entities.Light;
import fr.ostix.game.entities.Player;
import fr.ostix.game.font.meshCreator.FontType;
import fr.ostix.game.font.meshCreator.GUIText;
import fr.ostix.game.font.rendering.MasterFont;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.graphics.particles.MasterParticle;
import fr.ostix.game.graphics.particles.ParticleSystem;
import fr.ostix.game.graphics.particles.ParticleTexture;
import fr.ostix.game.graphics.render.GUIRenderer;
import fr.ostix.game.graphics.render.MasterRenderer;
import fr.ostix.game.graphics.render.WaterRenderer;
import fr.ostix.game.graphics.shader.WaterShader;
import fr.ostix.game.graphics.textures.ModelTexture;
import fr.ostix.game.gui.GUIGame;
import fr.ostix.game.gui.GUITexture;
import fr.ostix.game.postProcessing.Fbo;
import fr.ostix.game.postProcessing.PostProcessing;
import fr.ostix.game.world.Terrain;
import fr.ostix.game.world.texture.TerrainTexture;
import fr.ostix.game.world.texture.TerrainTexturePack;
import fr.ostix.game.world.water.WaterFrameBuffers;
import fr.ostix.game.world.water.WaterTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    public static final Logger LOGGER = LogManager.getLogger(Game.class);
    private boolean running = false;
    long timer = System.currentTimeMillis();

    public static boolean tick = false;
    public static boolean render = false;

    private List<Entity> entities;
    private List<Entity> normalMapEntities;
    private final List<Light> lights = new ArrayList<>();

    private final Loader loader = new Loader();
    private Camera cam;
    private MasterRenderer renderer;
    private Player player;
    private Terrain[][] world;
    private GUIRenderer guiRender;
    private GUIGame guiGame;
    private MousePicker picker;
    private Light sun;
    //      TEMP
    private Entity lamp;
    private Light light;

    //*********WATER*******
    private WaterRenderer waterRenderer;
    private List<WaterTile> waters;
    private WaterFrameBuffers waterFBOS;
    private GUITexture waterDepth;

    //********ParticleSystem*******
    private ParticleSystem playerParticle;

    //********PostProcessing*******
    private Fbo multiSampleFbo;
    private Fbo outputFbo;

    private void init() {
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grassy2").getId());
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud").getId());
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers").getId());
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/path").getId());

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap").getId());


        MeshModel model = OBJFileLoader.loadModel("lowPolyTree", loader);
        TextureModel treeModel = new TextureModel(model, new ModelTexture(loader.loadTexture("lowPolyTree")));
        TextureModel grassModel = new TextureModel(OBJFileLoader.loadModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("flower")));
        grassModel.getModelTexture().setTransparency(true).setUseFakeLighting(true);
        TextureModel fernModel = new TextureModel(OBJFileLoader.loadModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern")));
        fernModel.getModelTexture().setTransparency(true);
        TextureModel playerModel = new TextureModel(OBJFileLoader.loadModel("person", loader),
                new ModelTexture(loader.loadTexture("playerTexture")));
        TextureModel lamp = new TextureModel(OBJFileLoader.loadModel("lantern", loader),
                new ModelTexture(loader.loadTexture("lantern")).setInverseNormal(false).setSpecularMap(
                        loader.loadTexture("specularMap/lanternS").getId()
                ));

        Terrain terrain1 = new Terrain(0f, 0f, loader, texturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(1f, 0f, loader, texturePack, blendMap, "heightmap");
        Terrain terrain3 = new Terrain(0f, 1f, loader, texturePack, blendMap, "heightmap");
        Terrain terrain4 = new Terrain(1f, 1f, loader, texturePack, blendMap, "heightmap");


        world = new Terrain[2][2];
        world[0][0] = terrain1;
        world[1][0] = terrain2;
        world[0][1] = terrain3;
        world[1][1] = terrain4;


        entities = new ArrayList<>();
        normalMapEntities = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            float x = r.nextFloat() * 1600;
            float z = r.nextFloat() * 1600;
            entities.add(new Entity(treeModel, new Vector3f(x, getTerrain(world, x, z).getHeightOfTerrain(x, z), z),
                    0, 0, 0, 1.2f));
            x = r.nextFloat() * 1600;
            z = r.nextFloat() * 1600;
            entities.add(new Entity(grassModel, new Vector3f(x, getTerrain(world, x, z).getHeightOfTerrain(x, z), z),
                    0, 0, 0, 0.6f));
            x = r.nextFloat() * 1600;
            z = r.nextFloat() * 1600;
            entities.add(new Entity(fernModel, new Vector3f(x, getTerrain(world, x, z).getHeightOfTerrain(x, z), z),
                    0, 0, 0, 5f));
        }
        TextureModel barrelModel = new TextureModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getModelTexture().setShineDamper(10).setReflectivity(0.5f).setNormalMapID(loader.loadTexture("normalMap/barrelNormal").getId())
                .setSpecularMap(loader.loadTexture("specularMap/barrelS").getId());

        normalMapEntities.add(new Entity(barrelModel, new Vector3f(-75, 10, -75), 0, 0, 0, 1));

        this.lamp = new Entity(lamp, new Vector3f(100, getTerrain(world, 100, 0).getHeightOfTerrain(100, 0), 0), 0, 0, 0, 1);
        entities.add(this.lamp);
        entities.add(new Entity(lamp, new Vector3f(370, getTerrain(world, 370, 200).getHeightOfTerrain(370, 200), 200), 0, 0, 0, 1));

        player = new Player(playerModel, new Vector3f(10, 0, 10), 0, 0, 0, 1);
        entities.add(player);

        cam = new Camera(player);

        renderer = new MasterRenderer(loader, cam);

        //*******Light*******
        light = new Light(new Vector3f(100, getTerrain(world, 0, 0).getHeightOfTerrain(100, 0) + 11f, 0), new Color(1.5f, 0, 0), new Vector3f(0.9f, 0.01f, 0.002f));
        Light light2 = new Light(new Vector3f(370, getTerrain(world, 370, 200).getHeightOfTerrain(100, 200), 200), Color.CYAN);

        sun = new Light(new Vector3f(100000, 100000, -100000), Color.SUN);
        lights.add(light);
        lights.add(sun);
        lights.add(light2);
        //*******Gui*********
        List<GUITexture> guis = new ArrayList<>();
        GUITexture gui1 = new GUITexture(new ModelTexture(loader.loadTexture("gui/socuwan")), new Vector2f(0.9f, 0.9f), new Vector2f(0.1f, 0.1f));
        guis.add(gui1);
        guiRender = new GUIRenderer(loader);

        GUITexture guiHealth = new GUITexture(new ModelTexture(loader.loadTexture("gui/health")), new Vector2f(-0.5f, -0.5f), new Vector2f(0.25f, 0.25f));

        guiGame = new GUIGame(guis, guiHealth, guiRender, player);
        //guis.add(new GUITexture(renderer.getShadowMapTexture(),new Vector2f(0.5f,0.5f),new Vector2f(0.5f,0.5f)));


        picker = new MousePicker(renderer.getProjectionMatrix(), cam, world);


        //*************WATER************
        waters = new ArrayList<>();
        waters.add(new WaterTile(100, 120, -2));
        waterFBOS = new WaterFrameBuffers();


        WaterShader waterShader = new WaterShader();
        this.waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFBOS);
        waterDepth = new GUITexture(new ModelTexture(waterFBOS.getRefractionDepthTexture()), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        //guis.add(new GUITexture(new ModelTexture(waterFBOS.getRefractionTexture()), new Vector2f(0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
        //guis.add(waterDepth);

        //*********TEXT**********
        MasterFont.init(loader);
        FontType Liberty = new FontType(loader.loadTextureFont("candara"), "candara");
        GUIText text1 = new GUIText("Bienvenu dans ce jeu magique", 1f, Liberty, new Vector2f(0, 0.0f), 1f, true);
        text1.setColour(Color.RED);
        MasterFont.loadTexte(text1);

        //********PARTICLES*******
        MasterParticle.init(loader, renderer.getProjectionMatrix());

        ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particle/fire"), 8, true);
        playerParticle = new ParticleSystem(particleTexture, 15, 1.8f, 0.0f, 60 * 2f, 15);
        playerParticle.randomizeRotation();
        playerParticle.setLifeError(0.2f);
        playerParticle.setDirection(new Vector3f(0, 0.3f, 0), 0.001f);
        playerParticle.setSpeedError(0.5f);
        playerParticle.setScaleError(0.05f);

        //********PostProcessing*******
        PostProcessing.init(loader);
        multiSampleFbo = new Fbo(DisplayManager.getWidth(), DisplayManager.getHeight());
        outputFbo = new Fbo(DisplayManager.getWidth(), DisplayManager.getHeight(), Fbo.DEPTH_TEXTURE);

    }

    public void start() {
        LOGGER.info("start");
        running = true;
        DisplayManager.createDisplay();
        init();
        loop();
    }

    private void stop() {
        running = false;
    }

    public void exit() {
        outputFbo.cleanUp();
        multiSampleFbo.cleanUp();
        PostProcessing.cleanUp();
        MasterParticle.cleanUp();
        MasterFont.cleanUp();
        waterFBOS.cleanUp();
        waterRenderer.cleanUp();
        guiRender.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
        LOGGER.info("stop");
        System.exit(0);
    }

    private void loop() {

        long window = glfwGetCurrentContext();

        long before = System.nanoTime();
        long beforeRender = System.nanoTime();
        double elapsed;
        double elapsedRender;
        double nanoSeconds = 1000000000.0 / 60;
        double renderTime = 1000000000.0 / 30;

        int ticks = 0;
        int frames = 0;

        while (running) {
            if (glfwWindowShouldClose(window)) stop();

            tick = false;
            render = false;

            long now = System.nanoTime();
            elapsed = now - before;
            elapsedRender = now - beforeRender;

            if (elapsed > nanoSeconds) {
                before += nanoSeconds;
                update();
                ticks++;
            } else if (elapsedRender > renderTime) {
                render();
                DisplayManager.updateDisplay();
                frames++;
                beforeRender += renderTime;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                glfwSetWindowTitle(window, "Test " + ticks + " ticks " + frames + " fps");
                ticks = 0;
                frames = 0;
            }
        }
        exit();
    }

    private void update() {
        Input.updateInput(glfwGetCurrentContext());
        cam.move(world);
        player.move(world);
        picker.update();
        playerParticle.generateParticles(player.getPosition());
        MasterParticle.update(cam);
        Vector3f terraintPoint = picker.getCurrentTerrainPoint();
        if (terraintPoint != null) {
            this.lamp.setPosition(terraintPoint);
            this.light.setPosition(new Vector3f(terraintPoint.x, terraintPoint.y + 12.5f, terraintPoint.z-5));
        }

        guiGame.update();
        glfwPollEvents();
    }

    private void render() {

        renderer.renderShadowMap(entities, sun);

        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

        waterFBOS.bindReflectionFrameBuffer();
        float distance = 2 * (cam.getPosition().getY() - waters.get(0).getHeight());
        cam.getPosition().y -= distance;
        cam.invertPitch();
        renderer.renderScene(entities, normalMapEntities, world, lights, cam, new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 2f));
        cam.getPosition().y += distance;
        cam.invertPitch();

        waterFBOS.bindRefractionFrameBuffer();
        renderer.renderScene(entities, normalMapEntities, world, lights, cam, new Vector4f(0, -1, 0, waters.get(0).getHeight()));


        waterFBOS.unbindCurrentFrameBuffer();
        multiSampleFbo.bindFrameBuffer();
        renderer.renderScene(entities, normalMapEntities, world, lights, cam, new Vector4f(0, 1, 0, 100000));
        waterRenderer.render(waters, cam, sun);

        MasterParticle.render(cam);
        multiSampleFbo.unbindFrameBuffer();
        multiSampleFbo.resolveToFbo(outputFbo);
        PostProcessing.doPostProcessing(outputFbo.getColourTexture());

        MasterFont.render();
        guiGame.render();
    }


    private Terrain getTerrain(Terrain[][] terrains, float worldX, float worldZ) {
        int x = (int) (worldX / Terrain.getSIZE());
        int z = (int) (worldZ / Terrain.getSIZE());

        return terrains[x][z];
    }


}

