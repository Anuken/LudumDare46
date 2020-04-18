package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import ld.*;

public class Player extends Entity{
    public Dir facing = Dir.right;
    public Interval track = new Interval();

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y + 1, 8f);
    }

    @Override
    public void update(){
        Tmp.v1.setZero().set(Core.input.axis(Bind.move_x), Core.input.axis(Bind.move_y)).nor().scl(Game.speed);

        move(Tmp.v1.x, Tmp.v1.y);

        if(Tmp.v1.x > 0.0001f){
            facing = Dir.right;
        }else if(Tmp.v1.x < -0.0001f){
            facing = Dir.left;
        }else if(Tmp.v1.y < -0.0001f){
            facing = Dir.down;
        }else if(Tmp.v1.y > 0.0001f){
            facing = Dir.up;
        }

        if(track.get(2f) && !Tmp.v1.isZero()){
            float s = 1;
            Fx.track.at(x - s, y);
            Fx.track.at(x + s, y);
        }
    }

    @Override
    public void draw(){
        Draw.z(y);
        TextureRegion region = Core.atlas.find("player" + facing.suffix);
        Draw.rect(region, x, y + region.getHeight()/2f, region.getWidth() * (facing.flip ? -1 : 1), region.getHeight());
    }
}
