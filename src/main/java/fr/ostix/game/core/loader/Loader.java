package fr.ostix.game.core.loader;

import de.matthiasmann.twl.utils.PNGDecoder;
import fr.ostix.game.graphics.model.MeshModel;
import fr.ostix.game.graphics.textures.Texture;
import fr.ostix.game.graphics.textures.TextureData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class Loader {

    private static final Logger LOGGER = LogManager.getLogger(Loader.class);

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    /**
     * @param positions Tableaux des positions des Arrets
     * @param colors    Tableaux definisant la couleur de chaque arretes
     * @param indices   Tableaux d'indice pour optimiser le nombre d'arrete
     */
    public MeshModel loadToVAO(float[] positions, float[] colors, int[] indices) {
        int vaoID = createVAO();            //creation d'une addresse memoir pour le VAO
        bindIndicesBuffer(indices);         //Definition des indices dans une memoir tampon (Buffer)
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 4, colors);
        unbindVAO();
        return new MeshModel(vaoID, indices.length);
    }

    /**
     * @param positions     Tableaux des positions des Arrets
     * @param textureCoords Tableaux des coordonées de la texture par rapport au model
     * @param normals       Tableaux des Vecteur Normals
     * @param indices       Tableaux d'indice pour optimiser le nombre d'arrete
     */
    public MeshModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();            //creation d'une addresse memoir pour le VAO
        bindIndicesBuffer(indices);         //Definition des indices dans une memoir tampon (Buffer)
        storeDataInAttributeList(0, 3, positions);      //Integration des positions dans notre addresse memoir VAO avec comme index 0 et nombre de valeur par position 3
        storeDataInAttributeList(1, 2, textureCoords);      //Integration des positions de la texture dans notre addresse memoir VAO avec comme index 1 et nombre de valeur par position 2
        storeDataInAttributeList(2, 3, normals);    //Integration des vecteur normals dans notre addresse memoir VAO avec comme index 1 et nombre de valeur par position 3
        unbindVAO();
        return new MeshModel(vaoID, indices.length);
    }

    /**
     * @param positions Tableaux des positions des Arrets
     */
    public MeshModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();            //creation d'une addresse memoir pour le VAO
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new MeshModel(vaoID, positions.length / 2);
    }

    public Texture loadTexture(String textureName) {
        Texture texture = Texture.loadTexture("/textures/" + textureName + ".png");
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.2f);
        textures.add(texture.getId());
        return texture;
    }

    public int loadCubMap(String[] fileNames) {
        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < fileNames.length; i++) {
            TextureData data = decodeTextureFile(fileNames[i]);
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWigth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        textures.add(texID);
        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(Loader.class.getResource("/textures/skybox/" + fileName + ".png").toURI()));
            PNGDecoder decoder = new PNGDecoder(fis);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            fis.close();
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Tried to load " + fileName + " , didn't work", e);
            System.exit(-1);
        }
        return new TextureData(width, height, buffer);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();           //creation d'une addresse memoir pour le VAO
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);     //activation de la memoir prevu pour le model
        return vaoID;
    }

    private void storeDataInAttributeList(int attrib, int vecSize, float[] data) {
        int vbo = glGenBuffers();           //creation d'une addresse memoir pour le VBO
        vbos.add(vbo);                      //ajout de l'addresse memoir dans la liste de VertexBufferObject
        glBindBuffer(GL_ARRAY_BUFFER, vbo); //Activation de l'addresse memoir
        FloatBuffer buffer = createFloatBuffer(data);   //creation d'une memoir tampon (Buffer) du tableau a ajouter dans notre VAO
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);  //Definition des données dans une memoir tampon (Buffer)
        GL20.glVertexAttribPointer(attrib, vecSize, GL11.GL_FLOAT, false, 0, 0);     //Definition de l'index,nombre de donné a lire dans le tableau par arrete,type de variable,sont des vecteur normalizer ou pas dans la memoir tampon
        glBindBuffer(GL_ARRAY_BUFFER, 0);       //Desactivation du VBO actife
    }

    private void bindIndicesBuffer(int[] indices) {
        int vbo = glGenBuffers();           //creation d'une addresse memoir pour le VBO
        vbos.add(vbo);                      //ajout de l'addresse memoir dans la liste de VertexBufferObject
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);  //Activation de l'addresse memoir
        IntBuffer buffer = createIntBuffer(indices);     //creation d'une memoir tampon (Buffer) du tableau d'indices
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW); //Definition des indices dans une memoir tampon (Buffer)
    }

    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);  //Creation d'une memoir tampon avec la longeur du tableau
        buffer.put(data);         //On met les données dans la memoir tampon
        buffer.flip();              //Permet de lire la memoir tampon
        return buffer;
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);   //Creation d'une memoir tampon avec la longeur du tableau
        buffer.put(data);         //On met les données dans la memoir tampon
        buffer.flip();              //Permet de lire la memoir tampon
        return buffer;
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);      //Desactivation du VAO actife
    }

    public void cleanUp() {
        for (int i : vaos) {
            GL30.glDeleteVertexArrays(i);
        }
        for (int i : vbos) {
            GL30.glDeleteVertexArrays(i);
        }
        for (int i : textures) {
            GL11.glDeleteTextures(i);
        }
    }
}
