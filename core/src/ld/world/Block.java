package ld.world;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

import static ld.Game.tsize;

public enum Block{
    none,
    ice;

    TextureRegion[] regions;
    public int height;

    Block(){
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
    }

    public void draw(int x, int y, float elevation){
        if(regions.length != 0){
            TextureRegion reg = regions[Mathf.randomSeed(Pack.longInt(x, y), 0, regions.length - 1)];
            Draw.rect(reg, x * tsize, y * tsize + elevation + reg.getHeight()/2f);
        }
    }
}
