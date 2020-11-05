package fr.ostix.game.entities;

import fr.ostix.game.core.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private static final Logger LOGGER = LogManager.getLogger(Camera.class);

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private final Vector3f position = new Vector3f(100,35,0);
    float pitch = 20;
    float yaw = 0;
    private float roll;

    private final Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move(){
        caculateZoom();
        calculateAngleAroundPlayerAndPitch();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        caculateCameraPosition(horizontalDistance,verticalDistance);
        this.yaw = 180 - (player.getRoty() + angleAroundPlayer);

    }

    private void caculateCameraPosition(float horzontalDistance,float verticalDistance){
        float theta = player.getRoty() + angleAroundPlayer;
        float xoffset = (float) (horzontalDistance * Math.sin(Math.toRadians(theta)));
        float zoffset = (float) (horzontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x -xoffset;
        position.y = player.getPosition().y + 9 + verticalDistance;
        position.z = player.getPosition().z -zoffset;
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void caculateZoom(){
        float zoomLevel = Input.getMouseDWhell();
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer <= 3){
            distanceFromPlayer = 3;
        }
        if (distanceFromPlayer >= 125){
            distanceFromPlayer = 125;
        }
    }

    private void calculateAngleAroundPlayerAndPitch(){
        if (Input.keysMouse[GLFW_MOUSE_BUTTON_1]){
            float angleChange = Input.mouseDX* 0.1f;
            angleAroundPlayer -= angleChange;
            float pitchChange = Input.mouseDY* 0.1f;
            pitch += pitchChange;
            if (pitch >= 90){
                pitch = 90;
            }
            if (pitch <= -4){
                pitch = -4;
            }
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
