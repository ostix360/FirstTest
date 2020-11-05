package fr.ostix.game.world;

import fr.ostix.game.core.loader.Loader;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.math.Maths;
import fr.ostix.game.textures.TerrainTexture;
import fr.ostix.game.textures.TerrainTexturePack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Terrain {

    private static final Logger LOGGER = LogManager.getLogger(Terrain.class);

    private static final int SIZE = 800;
    private static final int MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOR = 256*256*256;

    private float[][] heights;
    private final float x;
    private final float z;
    private final MeshModel model;
    private final TerrainTexturePack texturePack;
    private final TerrainTexture blendMap;

    public Terrain(float gridX, float gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap,
                                                                                            String heightMap) {
        this.x = gridX *SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader,heightMap);
        this.texturePack = texturePack;
        this.blendMap = blendMap;
    }

    public float getHeightOfTerrain(float terrainX,float terrainZ,float gridSquareSize,int gridX,int gridZ) {
        float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
        float answer;
        if (xCoord <= (1-zCoord)) {
            answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    private MeshModel generateTerrain(Loader loader,String heigthMap){
        BufferedImage image = null;

        try {
            image = ImageIO.read(Terrain.class.getResource("/textures/terrain/" + heigthMap  + ".png"));
        } catch (IOException e) {
            LOGGER.error("Couldn't load heightMap " , e);
        }

        assert image != null;
        int VERTEX_COUNT = image.getHeight();
        heights =new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int z=0;z<VERTEX_COUNT;z++){            // Boucle de generation de monde
            for(int x=0;x<VERTEX_COUNT;x++){
                float height = getHeight(x,z,image);
                heights[x][z] = height;
                vertices[vertexPointer*3] = (float)x/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)z/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(x,z,image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)x/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)z/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){       //boucle de generation des indices
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x,int z,BufferedImage image){
        float heightL = getHeight(x-1,z,image);
        float heightR = getHeight(x+1,z,image);
        float heightD = getHeight(x,z-1,image);
        float heightU = getHeight(x,z+1,image);
        Vector3f normal = new Vector3f(heightL -heightR,2f,heightD-heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x,int z,BufferedImage image){
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x,z);
        height += MAX_PIXEL_COLOR/2;
        height /= MAX_PIXEL_COLOR/2;
        height *= MAX_HEIGHT;
        return height;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public float[][] getHeights() {
        return heights;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public MeshModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
