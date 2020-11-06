package fr.ostix.game.gui;

import fr.ostix.game.entities.Player;
import fr.ostix.game.graphics.render.GUIRenderer;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class GUIGame {

    private final GUITexture helthTexture;
    private final GUIRenderer renderer;
    private final Player player;
    List<GUITexture> guis;

    public GUIGame(List<GUITexture> guis, GUITexture helthTexture, GUIRenderer renderer, Player player) {
        this.guis = guis;
        this.helthTexture = helthTexture;
        this.renderer = renderer;
        this.player = player;
    }

    public void update() {
        for (int i = 0; i < 10; i++) {
            Vector2f position = helthTexture.getPosition();
            position.x += i * 0.003f;
            if (player.getHealth() > i) {
                helthTexture.setTextureIndex(1);
            } else {
                helthTexture.setTextureIndex(0);
            }
            helthTexture.setPosition(position);
        }
        guis.add(helthTexture);
    }

    public void render() {
        renderer.render(guis);
    }
}
