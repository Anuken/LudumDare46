package ld.entity;

import arc.*;
import arc.graphics.g2d.*;

public enum Item{
    log{{
        flammability = 0.2f;
        weight = 0.6f;
    }},
    stick{{
        flammability = 0.1f;
    }},
    rock{{
        damage = 15f;
        consumed = false;
        thrown = true;
        weight = 0.6f;
    }},
    axe{{
        damage = 20f;
        weight = 0.4f;
        yoffset = 1f;
        chopChance = 0.1f;
    }},
    fireAxe{{
        damage = 30f;
        weight = 0.4f;
        yoffset = 1f;
        chopChance = 0.2f;
    }},
    key,
    frozenKey;

    public float weight, damage, flammability, reload = 30f, yoffset, chopChance;
    public boolean consumed = false, thrown = false;

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
