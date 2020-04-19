package ld.entity;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

import static ld.Game.player;

public class SnowDemon extends Enemy{
    float attacklerp = 0f;
    {
        health = 50;
    }

    @Override
    public void update(){
        super.update();

        Tmp.v1.set(player).sub(this).limit(0.1f);
        move(Tmp.v1.x, Tmp.v1.y);

        if(within(player, 20f)){
            if(player.damagePeriodic(3f)){
                Fx.snowAttack.at(x, y + 1);
            }
        }

        attacklerp = Mathf.lerpDelta(attacklerp, Mathf.num(within(player, 20f)), 0.1f);
    }

    @Override
    public void draw(){

        Draw.z(y);
        float w = 26, h = Mathf.absin(8f, 6f) + 20 + Mathf.sin(4f, 5f*attacklerp);
        Draw.mixcol(hitColor, hitTime);
        Draw.rect("snowdemon", x, y + h/2, w, h);
    }
}
