package fr.ostix.game.entities;

import fr.ostix.game.graphics.Color;
import org.lwjgl.util.vector.Vector3f;

public class Light {
    private final Vector3f position;
    private final Color colour;
    private Vector3f attenuation = new Vector3f(1, 0, 0);


    public Light(Vector3f position, Color colour) {
        this.position = position;
        this.colour = colour;
    }

    public Light(Vector3f position, Color colour, Vector3f attenuation) {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColourVec3f() {
        return colour.getVec3f();
    }
}
