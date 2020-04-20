package ld;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import arc.util.noise.*;
import squidpony.squidgrid.mapping.*;
import squidpony.squidgrid.mapping.styled.*;

import static ld.Game.*;

public class World{
    public static final Tile none = new Tile();
    public int width, height;

    private Tile[] tiles;

    public World(){
        none.wall = Block.wall;
    }

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

        DungeonGenerator gen = new DungeonGenerator(width, height){{
            addDoors(1, false);
            addWater(30);
            addTraps(3);
            addGrass(8);
            addDoors(100, true);
        }};

        TilesetType[] valid = {TilesetType.CORNER_CAVES, TilesetType.SIMPLE_CAVES};

        char[][] out = gen.generate(Structs.random(valid));

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
                }

                if(c == '"'){
                    tile.floor = Block.stonefloor;

                    if(Mathf.chance(0.003) && !Mathf.within(x, y, width/2f, height/2, 60f)){
                        tile.floor = Block.teleporter;
                        for(Point2 p : Geometry.d8edge){
                            Tile other = tile(x + p.x, y + p.y);
                            if(other.exists()){
                               other.wall = Block.wall;
                            }
                        }
                    }

                    if(Mathf.chance(0.001)){
                        tile.item = Item.axe;
                    }
                }

                if(c == '^'){
                    if(Mathf.chance(0.3)){
                        tile.item = Mathf.chance(0.6) ? Item.stick : Item.log;
                    }else if(Mathf.chance(0.1)){
                        tile.item = Item.key;
                    }
                }

                if(c == '/' || c == '+'){
                    tile.wall = Block.door;
                }

                if(!tile.wall.solid && Mathf.chance(0.002)){
                    tile.wall = Block.crate;
                }

                if(!tile.wall.solid && Mathf.chance(0.002)){
                    tile.wall = Block.crate;
                }

                if(tile.floor == Block.ice && Mathf.chance(0.01)){
                    tile.item = Item.frozenStick;
                }

                if(tile.item == null && tile.wall == Block.none && Mathf.chance(0.01)){
                    tile.wall = Block.boulder;
                }
            }
        }

        //spawnpoint clear
        Geometry.circle(width/2, height/2, 10, (x, y) -> world.tile(x, y).wall = Block.none);

        Simplex noise = new Simplex(Mathf.random(9999));

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Tile tile = tile(x, y);

                if(noise.octaveNoise2D(1, 1, 1.0 / 34.0, x, y) > 0.75){
                    if(tile.wall.solid){
                        tile.wall = Block.rockwall;
                        if(Mathf.chance(0.05)){
                            tile.wall = Block.orewall;
                        }
                    }

                    outer:
                    for(Point2 p : Geometry.d4){
                        if(tile(x + p.x, y + p.y).wall.solid){
                            tile.floor = Block.rockfloor;

                            if(Mathf.chance(0.05)){
                                tile.item = Item.rock;
                            }

                            if(Mathf.chance(0.015)){
                                tile.item = Item.ore;
                            }
                            break outer;
                        }
                    }
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

                if(tile.floor == Block.snow && !tile.wall.solid && Mathf.chance(0.03)){
                    for(Point2 p : Geometry.d4){
                        if(tile(x + p.x, y + p.y).wall == Block.wall){
                            tile.wall = Block.tree;
                            continue outer;
                        }
                    }
                }

                if(tile.floor == Block.ice && !tile.wall.solid && Mathf.chance(0.005) && !Mathf.within(x, y, width/2, height/2, 90)){
                    for(Point2 p : Geometry.d4){
                        if(tile(x + p.x, y + p.y).floor == Block.ice){
                            tile.wall = Block.icecrystal;
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
        public Block floor = Block.snow;
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
