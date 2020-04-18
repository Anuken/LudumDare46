package ld.entity;

import ld.gfx.*;

public class Enemy extends Entity{
    public float health;

    public void damage(float amount){
        hitEffect().at(x, y + height());
        health -= amount;
        if(health <= 0){
            remove();
            killed();
            deathEffect().at(x, y + height());
        }
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
