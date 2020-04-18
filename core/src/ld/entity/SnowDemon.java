package ld.entity;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

import static ld.Game.player;

public class SnowDemon extends Enemy{
    {
        health = 10;
    }

    @Override
    public void update(){
        Tmp.v1.set(player).sub(this).limit(0.1f);
        move(Tmp.v1.x, Tmp.v1.y);
    }

    @Override
    public void draw(){
        Draw.z(y);
        float w = 26, h = Mathf.absin(8f, 6f) + 20;
        Draw.rect("snowdemon", x, y + h/2, w, h);
    }
}
