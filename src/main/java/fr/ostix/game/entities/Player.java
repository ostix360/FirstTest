package fr.ostix.game.entities;

import fr.ostix.game.core.Input;
import fr.ostix.game.graphics.model.TextureModel;
import fr.ostix.game.world.MasterTerrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final Logger LOGGER = LogManager.getLogger(Player.class);

    private static final float RUN_SPEED = 160;
    private static final float TURN_SPEED = 780;
    private static final float GRAVITY = -6;
    private static final float JUMP_POWER = 100;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public Player(TextureModel model, Vector3f position, float rotx, float roty, float rotz, float scale) {
        super(model, position, rotx, roty, rotz, scale);
    }


    public void move(MasterTerrain terrain) {
        checkInputs();
        super.increaseRotation(0, this.currentTurnSpeed * 0.0023f, 0);
        float distance = currentSpeed * 0.006f;
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRoty())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRoty())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY ;
        super.increasePosition(0, upwardsSpeed * 0.01f, 0);
        float terrainHeight = terrain.getTerrainHeight(super.getPosition().x,super.getPosition().z);
        if (getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            this.isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (Input.keys[GLFW_KEY_W] || Input.keys[GLFW_KEY_UP]) {
            this.currentSpeed = RUN_SPEED;
        } else if (Input.keys[GLFW_KEY_S] || Input.keys[GLFW_KEY_DOWN]) {
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (Input.keys[GLFW_KEY_A] || Input.keys[GLFW_KEY_LEFT]) {
            this.currentTurnSpeed = TURN_SPEED;
        } else if (Input.keys[GLFW_KEY_D] || Input.keys[GLFW_KEY_RIGHT]) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (Input.keys[GLFW_KEY_SPACE]) {
            this.jump();
        }
    }
}

