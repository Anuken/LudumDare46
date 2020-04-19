package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import ld.*;

import static ld.Game.*;

public class SnowDemon extends Enemy{
    static Array<Drop> drops = Array.with(
        new Drop(Item.frozenKey, 0.2f)
    );

    String name = "snowdemon";
    float attacklerp = 0f;
    {
        health = 40;
    }

    public float damage(){
        return 4f;
    }

    public float speed(){
        return 0.1f;
    }

    @Override
    public Array<Drop> drops(){
        return drops;
    }

    @Override
    public void update(){
        super.update();

        Tmp.v1.set(dst2(fire) > dst2(player) ? player : fire).sub(this).limit(speed());
        move(Tmp.v1.x, Tmp.v1.y);

        if(within(player, 20f)){
            if(player.damagePeriodic(damage())){
                Fx.snowAttack.at(x, y + 1);
                Sounds.hit.play(player);
            }
        }

        attacklerp = Mathf.lerpDelta(attacklerp, Mathf.num(within(player, 20f)), 0.1f);
    }

    @Override
    public void draw(){
        TextureRegion region = Core.atlas.find(name);

        Draw.z(y);
        float w = region.getWidth() * size, h = (Mathf.absin(8f, 6f) + 20 + Mathf.sin(4f, 5f*attacklerp)) * size;
        Draw.mixcol(hitColor, hitTime);
        Draw.rect(region, x, y + h/2, w, h);
    }
}
