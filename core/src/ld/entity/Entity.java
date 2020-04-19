package ld.entity;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import arc.util.*;
import ld.*;
import ld.World.*;
import ld.gfx.*;

import static ld.Game.*;

public class Entity implements Position, QuadTreeObject{
    private static int lastID = 0;

    public final int id = lastID++;
    public float x, y, telecharge;
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

    public boolean canTeleport(){
        return false;
    }

    public void move(Vec2 v){
        move(v.x, v.y);
    }

    public float interactX(){
        return x;
    }

    public float interactY(){
        return y;
    }

    public float clipSize(){
        return 60f;
    }

    public Position top(){
        return new Position(){
            @Override
            public float getX(){
                return x;
            }

            @Override
            public float getY(){
                return y + height();
            }
        };
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
        if(canTeleport()){
            updateTeleport();
        }
    }

    public void draw(){
        if(canTeleport() && telecharge > 0){
            Draw.z(0f);
            float cx = world.t(x) * tsize, cy = world.t(y) * tsize;
            Draw.color(Pal.fire2, Color.white, telecharge);
            float fout = 1f - telecharge;
            Lines.stroke(4f * telecharge);
            Lines.square(cx, cy, 35f * fout, 45);
            Lines.square(cx, cy, 15f * fout, 45);
            Draw.reset();
        }
    }

    public void drawShadow(){

    }

    public void clicked(){

    }

    public boolean clickable(){
        return false;
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

    public void updateTeleport(){
        //update teleporter
        Tile tile = tile();
        if(tile.floor == Block.teleporter){
            telecharge += Time.delta() / teleportDur;
            if(telecharge >= 1f){
                Fx.teleported.at(this);
                if(this == player){
                    ui.flash();
                }
                set(fire.x + Mathf.range(8f), fire.y - 20f + Mathf.range(8f));
                Fx.teleported.at(this);
            }
        }else{
            telecharge = 0f;
        }
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
