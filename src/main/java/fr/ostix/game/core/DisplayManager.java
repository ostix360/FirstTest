package fr.ostix.game.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {
    private static final Logger LOGGER = LogManager.getLogger(DisplayManager.class);

    private static int width = 1480;
    private static int height = 920;
    private static long window;

    private static float lastFrameTime;
    private static float delta;

    public static long createDisplay(){
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));


        if (!glfwInit()) throw new RuntimeException("Unable/Failed to Initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        String title = "Test";
        window = glfwCreateWindow(width, height, title, NULL, NULL);


        if (window == NULL) {
            glfwTerminate();
            LOGGER.error("Failed to create GLFW" + glfwGetClipboardString(window));
            throw new RuntimeException("Unable/Failed to Create GLFW Window");
        }//Check Window


        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(window,width,height);
            DisplayManager.width = width.get();
            DisplayManager.height = height.get();
        }

        /* Center the window on screen */
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        glfwSetWindowPos(window,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );
        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glfwSwapInterval(1);


        GL11.glViewport(0,0, width, height);
        return window;
    }

    public static void updateDisplay(){
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(window,width,height);
            DisplayManager.width = width.get();
            DisplayManager.height = height.get();
        }
        float currentFrameTime = getCurrentTime();
        delta = (currentFrameTime -lastFrameTime)*1000;
        lastFrameTime = getCurrentTime();
        GL11.glViewport(0,0, width, height);
        glfwSwapBuffers(window);
    }

    public static void closeDisplay(){
        LOGGER.debug("Exit");
        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static float getFrameTimeSeconde() {
        return delta;
    }

    private static float getCurrentTime(){
        return (float) (glfwGetTime()  *1000/ glfwGetTimerFrequency());
    }
}
