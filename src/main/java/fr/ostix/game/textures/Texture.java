package fr.ostix.game.textures;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Texture {

    private int width,height,id;

    public Texture(int width, int height, int id) {
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public static Texture loadTexture(String path)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(Texture.class.getResource(path));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        assert image != null;
        int w = image.getWidth();
        int h = image.getHeight();

        int[] pixels = new int[w*h];
        image.getRGB(0,0,w,h,pixels,0,w);

        ByteBuffer buffer = BufferUtils.createByteBuffer(w*h*4);

        for (int y = 0;y<w;y++)
        {
            for (int x = 0;x<h;x++)
            {
                int i = pixels[x+y*w];
                buffer.put((byte) ((i >> 16) & 0xFF));
                buffer.put((byte) ((i >> 8) & 0xFF));
                buffer.put((byte) ((i ) & 0xFF));
                buffer.put((byte) ((i >> 24) & 0xFF));
            }
        }

        buffer.flip();


        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,id);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);


        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);




        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,w,h,0,GL_RGBA,GL_UNSIGNED_BYTE,buffer);
        glBindTexture(GL_TEXTURE_2D,0);

        return new Texture(w,h, id);
    }

    public int getId() {
        return id;
    }
}
