package ld;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import ld.entity.Enemy.*;
import ld.entity.*;
import ld.gfx.*;

import static ld.Game.*;

public enum Block{
    none{
        public boolean interactable(Item item){
            return item != null && item.blockPlaced != null;
        }

        public void clicked(int x, int y, Item item){
            world.tile(x, y).wall = item.blockPlaced;
            if(item.blockPlaced.entityType != null){
                item.blockPlaced.entityType.get(new Point2(x, y));
            }
            player.item = null;
            Fx.pickup.at(x * tsize, y * tsize);
        }
    },
    ice,
    rockfloor,
    icewall(false){{
        breakable = true;
    }},
    torchBlock{
        {
            entityType = TorchEntity::new;
            prop = true;
            shadowsize = 4f;
        }

        @Override
        public boolean interactable(Item item){
            return item.chopChance > 0 || item.mineChance > 0;
        }

        @Override
        public void clicked(int x, int y, Item item){
            world.tile(x, y).wall = none;
            ItemEntity.create(Item.torch, x * tsize, y * tsize);
            Fx.pickup.at(x * tsize, y * tsize);
        }
    },
    gemTorchBlock{
        {
            entityType = GemTorchEntity::new;
            prop = true;
            shadowsize = 4f;
        }

        @Override
        public boolean interactable(Item item){
            return item.chopChance > 0 || item.mineChance > 0;
        }

        @Override
        public void clicked(int x, int y, Item item){
            world.tile(x, y).wall = none;
            ItemEntity.create(Item.torch, x * tsize, y * tsize);
            Fx.pickup.at(x * tsize, y * tsize);
        }
    },
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
    wall(false){{
        drops = new Drop[]{new Drop(Item.rock, 0.1)};
    }},
    boulder(false){{
        solid = false;
        drops = new Drop[]{new Drop(Item.rock, 1), new Drop(Item.rock, 0.3), new Drop(Item.coal, 0.15)};
        offset = 2;
        shadowsize = 11f;
        breakable = true;
        prop = true;
    }},
    rockwall(false){{
        drops = new Drop[]{new Drop(Item.rock, 0.2), new Drop(Item.coal, 0.1)};
        breakable = true;
    }},
    orewall(false){{
        drops = new Drop[]{new Drop(Item.ore, 1), new Drop(Item.coal, 0.1)};
        breakable = true;
    }},
    crate(false){{
        drops = new Drop[]{new Drop(Item.key, 0.5), new Drop(Item.stick, 0.3), new Drop(Item.ore, 0.5)};
        breakable = true;
    }},
    icecrystal{
        {
            prop = true;
            solid = false;
            offset = 4;
            shadowsize = 17;
            breakable = true;
            drops = new Drop[]{new Drop(Item.frozenKey, 0.9), new Drop(Item.frozenKey, 0.5), new Drop(Item.gem, 0.3), new Drop(Item.rock, 0.5)};
        }
    },
    tree(false){
        {
            shadowsize = 15;
            breakable = true;
            prop = true;
            solid = false;
            offset = 3;
            drops = new Drop[]{new Drop(Item.stick, 1f), new Drop(Item.stick, 0.7f), new Drop(Item.log, 0.6f), new Drop(Item.log, 0.4f)};
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
                drops(x, y);
                world.tile(x, y).wall = none;
            }
        }
    },
    teleporter,
    stonefloor;

    TextureRegion[] regions;

    public int height;
    public boolean trackable, solid, floor = true, prop, breakable = false;
    public float offset, shadowsize = 15;
    public Drop[] drops = {};
    public @Nullable Func<Point2, TileEntity> entityType;

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
        if(breakable){
            return item.mineChance > 0;
        }
        return false;
    }

    public void clicked(int x, int y, Item item){
        if(breakable){
            Fx.mine.at(x * tsize + Mathf.range(8f), y * tsize + 6f + Mathf.range(8f));
            player.attackTime = 1f;
            renderer.shake(2f);
            if(Mathf.chance(item.mineChance)){
                for(int i = 0; i < 6; i++){
                    Fx.mine.at(x * tsize + Mathf.range(10f), y * tsize + i * 3f + Mathf.range(6f));
                }
                renderer.shake(6f);

                drops(x, y);
                world.tile(x, y).wall = none;
            }
        }
    }

    void drops(int x, int y){
        for(Drop drop : drops){
            if(Mathf.chance(drop.chance)){
                ItemEntity.create(drop.item, x * tsize, y * tsize).velocity.rnd(Mathf.random(4f));
            }
        }
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
        if(prop){
            Drawf.shadow(x * tsize, y * tsize, shadowsize);
        }else if(solid){
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
