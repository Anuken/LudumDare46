package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import ld.*;

public class Player extends Entity{
    public Dir facing = Dir.right;

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y + 1, 8f);
    }

    @Override
    public void update(){
        Tmp.v1.setZero().set(Core.input.axis(Bind.move_x), Core.input.axis(Bind.move_y)).nor().scl(Game.speed);

        x += Tmp.v1.x;
        y += Tmp.v1.y;

        if(Tmp.v1.x > 0.0001f){
            facing = Dir.right;
        }else if(Tmp.v1.x < -0.0001f){
            facing = Dir.left;
        }else if(Tmp.v1.y < -0.0001f){
            facing = Dir.down;
        }else if(Tmp.v1.y > 0.0001f){
            facing = Dir.up;
        }
    }

    @Override
    public void draw(){
        TextureRegion region = Core.atlas.find("player" + facing.suffix);
        Draw.rect(region, x, y + region.getHeight()/2f, region.getWidth() * (facing.flip ? -1 : 1), region.getHeight());
    }
}
