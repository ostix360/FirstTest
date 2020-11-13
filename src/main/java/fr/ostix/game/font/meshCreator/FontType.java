package fr.ostix.game.font.meshCreator;

public class FontType {

	private final int textureAtlas;
	private final TextMeshCreator loader;


	public FontType(int textureAtlas, String fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile);
	}

	public int getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshData loadText(GUIText text) {
		return loader.createTextMesh(text);
	}

}
