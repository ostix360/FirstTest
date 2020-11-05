package fr.ostix.game.core;


import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input{

    public static boolean[] keys =new boolean[65535];
    public static boolean[] keysMouse = new boolean[65535];

    public static DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
    public static DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);

    public static float mouseDY;
    public static float mouseDX;
    public static float mouseDWhell;
    private static float beforePositionX;
    private static float beforePositionY;

    public static void updateInput(long window){
        mouseDY = 0;
        mouseDX = 0;
        //mouseDWhell = 0;

        glfwGetCursorPos(window, b1, b2);
        mouseDX = (float) b1.get() - beforePositionX;
        mouseDY = (float) b2.get() - beforePositionY;


        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = action != GLFW_RELEASE;
            }
        });

        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                keysMouse[button] = action != GLFW_RELEASE;
            }
        });

        glfwSetScrollCallback(window,(w, xoffset, yoffset) ->{
            mouseDWhell = (float) (yoffset - xoffset);
        });

        b1.flip();
        b2.flip();

        beforePositionX = (float) b1.get();
        beforePositionY = (float) b2.get();

        b1.flip();
        b2.flip();
    }


    public static float getMouseDWhell() {
        float value = mouseDWhell;
        mouseDWhell = 0;
        return value;
    }
}
