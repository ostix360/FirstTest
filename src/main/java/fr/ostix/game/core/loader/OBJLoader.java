package fr.ostix.game.core.loader;

import fr.ostix.game.graphics.model.MeshModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    private static final Logger LOGGER = LogManager.getLogger(OBJLoader.class);

    public static MeshModel loadModel(String fileName , Loader loader){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(OBJLoader.class.getResource("/obj/"+fileName+".obj").toURI())));
           } catch (FileNotFoundException | URISyntaxException e) {
            LOGGER.error("Couldn't found file");
            e.printStackTrace();
        }
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;
        try{
            while (true){
                assert br != null;
                line = br.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")){
                    Vector3f vertx = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2])
                                                ,Float.parseFloat(currentLine[3]));
                    vertices.add(vertx);
                }else if(line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }else if(line.startsWith("vn ")){
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2])
                            ,Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }else if (line.startsWith("f ")){
                    texturesArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }

            while (line != null){
                if (!line.startsWith("f ")){
                    line = br.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,texturesArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,texturesArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,texturesArray,normalsArray);
                line = br.readLine();
            }
            br.close();
        }catch (Exception e){
            LOGGER.error("Couldn't load file",e);
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i ++){
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray,texturesArray,normalsArray,indicesArray);
    }

    private static void processVertex(String[] vertexData,List<Integer> indices,List<Vector2f> textures,
                                      List<Vector3f> normals, float[] textrueArray,float[] normalsArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
        indices.add(currentVertexPointer);
        Vector2f currentTexturePointer = textures.get(Integer.parseInt(vertexData[1])-1);
        textrueArray[currentVertexPointer *2] = currentTexturePointer.x;
        textrueArray[currentVertexPointer *2 + 1] = 1 - currentTexturePointer.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;

    }
}
