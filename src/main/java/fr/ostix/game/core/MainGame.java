package fr.ostix.game.core;

import fr.ostix.game.core.loader.OBJFileLoader;
import fr.ostix.game.entities.Camera;
import fr.ostix.game.entities.Entity;
import fr.ostix.game.entities.Player;
import fr.ostix.game.graphics.Color;
import fr.ostix.game.entities.Light;
import fr.ostix.game.graphics.render.MasterRenderer;
import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.textures.ModelTexture;
import fr.ostix.game.textures.TerrainTexture;
import fr.ostix.game.textures.TerrainTexturePack;
import fr.ostix.game.world.Terrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGame {

    private static final Logger LOGGER = LogManager.getLogger(MainGame.class);

   /* public static void main(String[] args) {
       long window =  DisplayManager.createDisplay();

        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2").getId());
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud").getId());
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers").getId());
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path").getId());

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap").getId());


        MeshModel model = OBJFileLoader.loadModel("lowPolyTree",loader);
        TextureModel treeModel = new TextureModel(model,new ModelTexture(loader.loadTexture("lowPolyTree")));
        TextureModel grassModel = new TextureModel(OBJFileLoader.loadModel("grassModel",loader),
                new ModelTexture(loader.loadTexture("flower")));
        grassModel.getModelTexture().setTransparency(true).setUseFakeLighting(true);
        TextureModel fernModel = new TextureModel(OBJFileLoader.loadModel("fern",loader),
                new ModelTexture(loader.loadTexture("fern")));
        fernModel.getModelTexture().setTransparency(true);
        TextureModel playerModel = new TextureModel(OBJFileLoader.loadModel("stanfordBunny",loader),
                new ModelTexture(loader.loadTexture("white")));

        List<Entity> forest = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 500;i++){
            forest.add(new Entity(treeModel,new Vector3f(r.nextFloat()*1600-800,0,r.nextFloat()*1600-800),
                                            0,0,0,1.2f));
            forest.add(new Entity(grassModel,new Vector3f(r.nextFloat()*1600-800,0,r.nextFloat()*1600-800),
                                            0,0,0,0.6f));
            forest.add(new Entity(fernModel,new Vector3f(r.nextFloat()*1600-800,0,r.nextFloat()*1600-800),
                                            0,0,0,0.3f));
        }

        Player player = new Player(playerModel,new Vector3f(100,0,-100),0,0,0,1);

        Light light = new Light(new Vector3f(0,600,600),Color.WHITE);

        Terrain terrain = new Terrain(-0.5f,-0.5f,loader,texturePack,blendMap);

        Camera cam = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while (!glfwWindowShouldClose(window)){
            cam.move();
            player.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            for (Entity e : forest){
                renderer.processEntity(e);
            }
            renderer.render(light,cam);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUP();
        DisplayManager.closeDisplay();
    }*/

    public static void main(String[] args) {
        new Game().start();
    }
}
