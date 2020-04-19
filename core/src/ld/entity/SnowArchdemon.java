package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import ld.*;

public class SnowArchdemon extends SnowDemon{
    static Array<Drop> drops = Array.with(new Drop(Item.frozenKey, 0.3), new Drop(Item.stick, 0.5));

    {
        health = 60;
        name = "snowarchdemon";
    }

    @Override
    public Array<Drop> drops(){
        return drops;
    }

    @Override
    public float speed(){
        return 0.16f;
    }

    @Override
    public float damage(){
        return 6f;
    }

    @Override
    public void draw(){
        TextureRegion region = Core.atlas.find(name);

        Draw.z(y);
        float w = region.getWidth() * size, h = region.getHeight() * size;
        Draw.mixcol(hitColor, hitTime);
        Draw.rect(region, x, y + h/2, w, h, w/2f, 0, Mathf.sin(Time.time(), 15f, 9f) * (1f + attacklerp));
    }
}
