package ld.entity;

import arc.*;
import arc.graphics.g2d.*;

public enum Item{
    log,
    stick{{
        flammability = 0.1f;
    }};

    public TextureRegion region;
    public float flammability;

    Item(float flammability){
        region = Core.atlas.find(name());
        this.flammability = flammability;
    }

    Item(){
        this(0);
    }
}
