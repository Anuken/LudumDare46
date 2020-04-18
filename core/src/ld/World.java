package ld;

import arc.func.*;
import arc.math.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import ld.world.*;

import static ld.Game.tsize;

public class World{
    public static final Tile none = new Tile();
    public int width, height;

    private Tile[] tiles;

    public int t(float f){
        return Mathf.round(f / tsize);
    }

    public float uwidth(){
        return width * tsize;
    }

    public float uheight(){
        return height * tsize;
    }

    public void resize(int width, int height){
        tiles = new Tile[width * height];
        this.width = width;
        this.height = height;

        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new Tile();
        }
    }

    public void each(Intc2 cons){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                cons.get(x, y);
            }
        }
    }

    public @NonNull Tile tile(int x, int y){
        if(!Structs.inBounds(x, y, width, height)){
            return none;
        }
        return tiles[y * width + x];
    }

    public static class Tile{
        public Block floor = Block.ice;
        public Block top = Block.none;

        public boolean exists(){
            return this != none;
        }
    }
}
