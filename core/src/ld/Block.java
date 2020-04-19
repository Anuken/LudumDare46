package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import ld.entity.*;

import static ld.Game.*;

public enum Block{
    none,
    ice,
    icewall(false),
    door(false){
        {
            solid = true;
        }

        public boolean interactable(Item item){
            return item == Item.key;
        }

        public void clicked(int x, int y, Item item){
            world.tile(x, y).wall = Block.none;
            player.item = null;
            Fx.unlock.at(x * tsize, y * tsize);
        }
    },
    snow{{
        trackable = true;
    }},
    wall(false),
    crate(false){{
    }},
    teleporter,
    stonefloor;

    TextureRegion[] regions;

    public int height;
    public boolean trackable, solid, floor = true;

    private boolean init = false;

    Block(){

    }

    Block(boolean floor){
        this.floor = floor;
        if(!floor){
            solid = true;
        }
    }

    public boolean interactable(Item item){
        return false;
    }

    public void clicked(int x, int y, Item item){

    }

    private void checkInit(){
        if(!init){
            int found = 0;
            for(; found < 10; found++){
                if(!Core.atlas.has(name() + (found+1))){
                    break;
                }
            }

            if(found == 0){
                regions = Core.atlas.has(name()) ? new TextureRegion[]{Core.atlas.find(name())} : new TextureRegion[0];
            }else{
                regions = new TextureRegion[found];
                for(int i = 0; i < found; i++){
                    regions[i] = Core.atlas.find(name() + (i+1));
                }
            }

            if(regions.length > 0){
                height = regions[0].getHeight() - regions[0].getWidth();
            }
            init = true;
        }
    }

    public void draw(int x, int y){
        checkInit();

        if(regions.length != 0){
            Draw.z(y * tsize - tsize/2f);

            TextureRegion reg = regions[Mathf.randomSeed(Pack.longInt(x, y), 0, regions.length - 1)];
            float offset = floor ? 0f : -tsize/2f + reg.getHeight()/2f;
            Draw.rect(reg, x * tsize, y * tsize + offset);
        }
    }
}
