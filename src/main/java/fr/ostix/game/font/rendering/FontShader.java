package fr.ostix.game.font.rendering;


import fr.ostix.game.graphics.shader.ShaderProgram;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram {

    private int location_color;
    private int location_translation;

    public FontShader() {
        super("font.vert", "font.frag");
    }

    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_translation = super.getUniformLocation("translation");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    public void loadColor(Vector3f color) {
        super.loadVerctor3fToUniform(location_color, color);
    }

    public void loadTranslation(Vector2f translation) {
        super.loadVerctor2fToUniform(location_translation, translation);
    }
}
