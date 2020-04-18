package ld.entity;

import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import ld.World.*;

import static ld.Game.*;

public class Entity implements Position, QuadTreeObject{
    private static int lastID = 0;

    public final int id = lastID++;
    public float x, y;
    public boolean added;

    public Tile tile(){
        return world.tile(world.t(x), world.t(y));
    }

    public boolean solid(){
        return false;
    }

    public float height(){
        return 8f;
    }

    public void move(Vec2 v){
        move(v.x, v.y);
    }

    public void move(float x, float y){
        Collisions.move(this, x, y);
    }

    public void hitbox(Rect rect){
        float w = 8f, h = 8f;
        rect.set(x - w/2f, y, w, h);
    }

    public Rect tileHit(Rect rect){
        float w = 8f, h = 3f;
        return rect.set(x - w/2f, y, w, h);
    }

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
