package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.ArcAnnotate.*;

public enum Item{
    log{{
        flammability = 0.3f;
        weight = 0.6f;
    }},
    stick{{
        flammability = 0.2f;
    }},
    frozenStick{{
        flammability = 0.1f;
    }},
    rock{{
        damage = 14f;
        consumed = false;
        thrown = true;
        weight = 0.6f;
    }},
    coal{{
        flammability = 0.5f;
    }},
    glowingRock{{
        damage = 20f;
        consumed = false;
        thrown = true;
        weight = 0.6f;
    }},
    ore{{
        damage = 18f;
        consumed = false;
        thrown = true;
        weight = 0.65f;
    }},
    torch{{
        blockPlaced = Block.torchBlock;
        damage = 25f;
        consumed = false;
        thrown = true;
    }},
    gemTorch{{
        blockPlaced = Block.gemTorchBlock;
        damage = 35f;
        consumed = false;
        thrown = true;
    }},
    gem{{

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
    pickaxe{{
        damage = 14f;
        weight = 0.4f;
        mineChance = 0.08f;
    }},
    firePickaxe{{
        damage = 25f;
        weight = 0.4f;
        yoffset = 1f;
        mineChance = 0.16f;
    }},
    gemTool{{
        damage = 35f;
        weight = 0.4f;
        yoffset = 1f;
        mineChance = 0.25f;
        chopChance = 0.3f;
        reload = 20f;
    }},
    key,
    frozenKey;

    public float weight, damage, flammability, reload = 30f, yoffset, chopChance, mineChance;
    public boolean consumed = false, thrown = false;
    public @Nullable Block blockPlaced;

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
