package ld.entity;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.gfx.*;

import static ld.Game.renderer;

public class Enemy extends Entity{
    public float health, hitTime;
    public static final Color hitColor = Pal.fire2;

    public void damage(float amount){
        hitEffect().at(x, y + height());
        health -= amount;
        hitTime = 1f;
        if(health <= 0){
            remove();
            killed();
            deathEffect().at(x, y + height());
            renderer.shake(2f);
        }
    }

    public void hitbox(Rect rect){
        float w = 8f, h = 16f;
        rect.set(x - w/2f, y, w, h);
    }

    @Override
    public void update(){
        super.update();

        hitTime -= Time.delta() / 20f;
        hitTime = Mathf.clamp(hitTime);
    }

    @Override
    public boolean solid(){
        return true;
    }

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y, 10f);
    }

    public void killed(){

    }

    public Effect hitEffect(){
        return Fx.hitsnow;
    }

    public Effect deathEffect(){
        return Fx.deathsnow;
    }
}
