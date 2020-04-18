package ld.entity;

import arc.*;
import arc.graphics.g2d.*;

public enum Item{
    log,
    stick{{
        flammability = 0.1f;
    }};

    public float flammability;

    Item(float flammability){
        this.flammability = flammability;
    }

    Item(){
        this(0);
    }

    public TextureRegion region(){
        return Core.atlas.find(name());
    }
}
