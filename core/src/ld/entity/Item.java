package ld.entity;

import arc.*;
import arc.graphics.g2d.*;

public enum Item{
    log{{
        flammability = 0.2f;
        weight = 0.5f;
        damage = 5;
    }},
    stick{{
        flammability = 0.1f;
    }},
    rock{{
        damage = 15f;
        consumed = false;
        weight = 0.5f;
    }},
    axe{{
        damage = 20f;
    }},
    key,
    frozenKey;

    public float flammability;
    public float damage;
    public float weight;
    public boolean consumed = false;

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
