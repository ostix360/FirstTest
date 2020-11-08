package fr.ostix.game.core;


import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static final DoubleBuffer MOUSE_X = BufferUtils.createDoubleBuffer(1);
    public static boolean[] keysMouse = new boolean[65535];
    private static final DoubleBuffer MOUSE_Y = BufferUtils.createDoubleBuffer(1);
    public static boolean[] keys = new boolean[65535];
    private static double mouseX;
    private static double mouseY;
    public static float mouseDY;
    public static float mouseDX;
    public static float mouseDWhell;
    private static float beforePositionX;
    private static float beforePositionY;

    public static void updateInput(long window) {
        mouseDY = 0;
        mouseDX = 0;
        //mouseDWhell = 0;

        glfwGetCursorPos(window, MOUSE_X, MOUSE_Y);
        mouseX = MOUSE_X.get();
        mouseY = MOUSE_Y.get();
        mouseDX = (float) mouseX - beforePositionX;
        mouseDY = (float) mouseY - beforePositionY;


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

        glfwSetScrollCallback(window, (w, xoffset, yoffset) -> mouseDWhell = (float) (yoffset - xoffset));

        MOUSE_X.flip();
        MOUSE_Y.flip();

        beforePositionX = (float) MOUSE_X.get();
        beforePositionY = (float) MOUSE_Y.get();

        MOUSE_X.flip();
        MOUSE_Y.flip();
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static float getMouseDWhell() {
        float value = mouseDWhell;
        mouseDWhell = 0;
        return value;
    }
}
