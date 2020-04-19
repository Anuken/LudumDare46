package ld;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import arc.util.noise.*;
import ld.entity.*;
import squidpony.squidgrid.mapping.*;
import squidpony.squidgrid.mapping.styled.*;

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

            tiles[i].floor = Block.snow;

            if(Mathf.chance(0.1)){
                //tiles[i].wall = Block.wall;
            }
        }

        char[][] out;

        //FlowingCaveGenerator
        DungeonGenerator gen = new DungeonGenerator(width, height){{
            addDoors(1, false);
            addWater(30);
            addTraps(3);
            addGrass(8);
            addDoors(100, true);
        }};

        FlowingCaveGenerator flow = new FlowingCaveGenerator(width, height);

        out = gen.generate(TilesetType.CORNER_CAVES);
        display(DungeonUtility.hashesToLines(out));

        //char[][] out = gen.generate();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Tile tile = tile(x, y);
                char c = out[x][y];

                tile.floor = Block.snow;

                if(c == '#'){
                    tile.wall = Block.wall;
                }

                if(c == ',' || c == '~'){
                    tile.floor = Block.ice;

                    if(Mathf.chance(0.01)){
                        tile.item = Item.rock;
                    }
                }

                if(c == '"'){
                    tile.floor = Block.stonefloor;
                }

                if(c == '^'){
                    if(Mathf.chance(0.4)){
                        tile.item = Mathf.chance(0.6) ? Item.stick : Item.log;
                    }else if(Mathf.chance(0.1)){
                        tile.item = Item.key;
                    }
                }

                if(c == '/' || c == '+'){
                    tile.wall = Block.door;
                }

                if(Noise.nnoise(x, y, 30f, 1f) > 0.5f){
                    //tile.floor = Block.snow;
                }
            }
        }

        //postprocess
        for(int x = 0; x < width; x++){
            outer:
            for(int y = 0; y < height; y++){
                Tile tile = tile(x, y);

                if(tile.wall.solid){
                    for(Point2 p : Geometry.d4){
                        if(tile(x + p.x, y + p.y).floor == Block.ice){
                            tile.wall = Block.icewall;
                            continue outer;
                        }
                    }
                }
            }
        }
    }

    void display(char[][] map){
        StringBuilder result = new StringBuilder(width * height);
        for(int y = 0; y < map.length; y++){
            StringBuilder out = new StringBuilder(map[0].length);
            for(int x = 0; x < map[0].length; x++){
                out.append(map[x][y]);
            }
            result.append(out).append("\n");
        }
        Log.info(result);
    }

    public void each(Intc2 cons){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                cons.get(x, y);
            }
        }
    }

    public @NonNull Tile tilew(float x, float y){
        return tile(t(x), t(y));
    }

    public @NonNull Tile tile(int x, int y){
        if(!Structs.inBounds(x, y, width, height)){
            return none;
        }
        return tiles[y * width + x];
    }

    public static class Tile{
        public Block floor = Block.ice;
        public Block wall = Block.none;
        public @Nullable Item item;

        public boolean solid(){
            return wall.solid;
        }

        public boolean exists(){
            return this != none;
        }
    }
}
