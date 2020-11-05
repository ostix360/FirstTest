package fr.ostix.game.world;

import fr.ostix.game.graphics.render.MasterRenderer;

import java.util.List;

public class MasterTerrain {

    private final List<Terrain> terrains;

    public MasterTerrain(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public float getTerrainHeight(float x,float z){
        for (Terrain terrain: terrains){
            float terrainX = x-terrain.getX();
            float terrainZ = z-terrain.getZ();
            float gridSquareSize = Terrain.getSIZE() / ((float) terrain.getHeights().length -1);  // cacul de la grille donc nombre de vertex - 1
            int gridX = (int) Math.floor(terrainX/gridSquareSize);
            int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
            if (gridX < 0 || gridX >= terrain.getHeights().length -1 || gridZ < 0 || gridZ >= terrain.getHeights().length-1){
                  continue;
            }
            return terrain.getHeightOfTerrain(terrainX,terrainZ,gridSquareSize,gridX,gridZ);
        }
        return 0;
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }
}
