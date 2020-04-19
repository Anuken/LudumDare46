package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import ld.*;

public class Crystal extends SnowDemon{
    static Array<Drop> drops = Array.with(new Drop(Item.frozenKey, 0.2f));

    {
        name = "crystal";
        health = 20;
    }

    @Override
    public Array<Drop> drops(){
        return drops;
    }

    @Override
    public float speed(){
        return 0.5f;
    }

    @Override
    public void draw(){
        TextureRegion region = Core.atlas.find(name);

        Draw.z(y);
        float w = region.getWidth() * size, h = region.getHeight();
        Draw.mixcol(hitColor, hitTime);
        Draw.rect(region, x, y + h/2 + Mathf.absin(10f, 3f) - 2, w, h, Mathf.sin(3f, 10f) * attacklerp);
    }
}
