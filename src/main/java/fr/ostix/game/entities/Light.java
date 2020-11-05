package fr.ostix.game.entities;

import fr.ostix.game.graphics.Color;
import org.lwjgl.util.vector.Vector3f;

public class Light {
    private Vector3f position;
    private Color colour;

    public Light(Vector3f position, Color colour) {
        this.position = position;
        this.colour = colour;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColourVec3f() {
        return colour.getVec3f();
    }
}
