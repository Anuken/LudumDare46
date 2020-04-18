package ld.entity;

import arc.math.geom.*;

import static ld.Game.control;

public class Entity implements Position{
    public float x, y;
    public boolean added;

    public void update(){

    }

    public void draw(){

    }

    public void drawShadow(){

    }

    public void add(){
        if(!added){
            control.add(this);
            added = true;
        }
    }

    public void remove(){
        if(added){
            control.remove(this);
            added = false;
        }
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX(){
        return x;
    }

    @Override
    public float getY(){
        return y;
    }
}
