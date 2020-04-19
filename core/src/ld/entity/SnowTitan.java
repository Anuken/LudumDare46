package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import ld.*;

public class SnowTitan extends SnowDemon{
    static Array<Drop> drops = Array.with(new Drop(Item.frozenKey, 0.3), new Drop(Item.stick, 0.1), new Drop(Item.rock, 0.3));

    {
        health = 80;
        name = "snowtitan";
    }

    @Override
    public Array<Drop> drops(){
        return drops;
    }

    @Override
    public float speed(){
        return 0.09f;
    }

    @Override
    public float damage(){
        return 8f;
    }

    @Override
    public void draw(){
        TextureRegion region = Core.atlas.find(name);

        Draw.z(y);
        float w = region.getWidth() * size, h = (Mathf.absin(8f, 7f) + 26 + Mathf.sin(4f, 5f*attacklerp)) * size;
        Draw.mixcol(hitColor, hitTime);
        Draw.rect(region, x, y + h/2, w, h, w/2f, 0, Mathf.sin(Time.time(), 15f, 9f) * (1f + attacklerp));
    }
}
