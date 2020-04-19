package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import ld.entity.*;
import ld.gfx.*;

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
    tree(false){
        {
            prop = true;
            solid = false;
            offset = 3;
        }

        @Override
        public void drawShadow(int x, int y){
            Drawf.shadow(x * tsize, y * tsize, 15f);
        }

        @Override
        public boolean interactable(Item item){
            return item.chopChance > 0;
        }

        @Override
        public void clicked(int x, int y, Item item){
            Fx.chop.at(x * tsize, y * tsize + 10f);
            player.attackTime = 1f;
            renderer.shake(2f);
            if(Mathf.chance(item.chopChance)){
                for(int i = 0; i < 6; i++){
                    Fx.chop.at(x * tsize + Mathf.range(4f), y * tsize + i * 9f + Mathf.range(4f));
                }
                renderer.shake(6f);
                int amount = Mathf.random(1, 4);
                for(int i = 0; i < amount; i++){
                    ItemEntity.create(Mathf.chance(0.6) ? Item.stick : Item.log, x * tsize, y * tsize).velocity.rnd(Mathf.random(4f));
                }
                world.tile(x, y).wall = none;
            }
        }
    },
    teleporter,
    stonefloor;

    TextureRegion[] regions;

    public int height;
    public boolean trackable, solid, floor = true, prop;
    public float offset;

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

    public void drawShadow(int x, int y){
        if(solid){
            Draw.rect("wallshadow", x * tsize, y * tsize);
        }
    }

    public void draw(int x, int y){
        checkInit();

        if(regions.length != 0){
            TextureRegion reg = regions[Mathf.randomSeed(Pack.longInt(x, y), 0, regions.length - 1)];

            if(prop){
                Draw.z(y * tsize);

                float offset = reg.getHeight() / 2f - this.offset;
                Draw.rect(reg, x * tsize, y * tsize + offset);
            }else{
                Draw.z(y * tsize - tsize / 2f);

                float offset = floor ? 0f : -tsize / 2f + reg.getHeight() / 2f;
                Draw.rect(reg, x * tsize, y * tsize + offset);
            }
        }
    }
}
