package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.ArcAnnotate.*;
import ld.*;
import ld.gfx.*;

import static ld.Game.*;

public class Player extends Entity{
    static int index = 0;
    static final float blinkDuration = 5f;

    public Dir facing = Dir.right, lastFacing = facing;
    public Interval time = new Interval(4);

    public @Nullable Item item;
    public float heat = 1f, moveTime;

    Vec2 movement = new Vec2();
    float faceFade = 0f, blinkTime;

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y + 1, 8f);
    }

    @Override
    public void update(){
        Tmp.v1.setZero().set(Core.input.axis(Bind.move_x), Core.input.axis(Bind.move_y)).nor().scl(Game.speed);

        move(Tmp.v1.x, Tmp.v1.y);
        Dir prev = facing;

        if(Tmp.v1.x > 0.0001f){
            facing = Dir.right;
        }else if(Tmp.v1.x < -0.0001f){
            facing = Dir.left;
        }else if(Tmp.v1.y < -0.0001f){
            facing = Dir.down;
        }else if(Tmp.v1.y > 0.0001f){
            facing = Dir.up;
        }

        if(prev != facing){
            faceFade = 1f;
            lastFacing = facing;
        }

        if(time.get(0, 2f) && !Tmp.v1.isZero()){
            float s = 1;
            Fx.track.at(x - s, y);
            Fx.track.at(x + s, y);
        }

        if(!Tmp.v1.isZero()){
            moveTime += Time.delta();
        }

        blinkTime -= Time.delta();

        if(time.get(2, 60f * 2.9f) && Mathf.chance(0.6)){
            blinkTime = blinkDuration;
        }

        movement.lerpDelta(Tmp.v1.nor(), 0.14f);

        heat -= (1f + control.windStrength()) / heatDuration;
    }

    @Override
    public void draw(){
        Draw.z(y);

        renderer.beginOutline();
        drawDirection(facing);
        renderer.endOutline();

        if(time.get(1, 70f) && Mathf.chance(0.3)){
            float rand = 2f, scl = 4f;
            Fx.breath.at(x + facing.direction.x*scl + Mathf.range(rand), y + facing.direction.y*scl + Mathf.range(rand) + 11f);
        }
    }

    void drawDirection(Dir facing){
        TextureRegion region = Core.atlas.find("player" + facing.suffix);
        TextureRegion hair = Core.atlas.find("hair" + facing.suffix);
        TextureRegion eyes = Core.atlas.find("eyes" + facing.suffix);

        float cx = x, cy = y + region.getHeight()/2f, cw = region.getWidth() * (facing.flip ? -1 : 1), ch = region.getHeight();
        Runnable drawHair = () -> {
            Draw.rect(hair, cx, cy, cw, ch);

            index = 0;

            Draw.rectv(hair, cx, cy, cw, ch, v -> {
                float scl = 7f + index%2, mag = 2f, movescl = -2.5f;

                if((!facing.y && (index == 0 || index == 1)) || (facing.y && (index == 0 || index == 3))){
                    v.add(Mathf.sin(index*5.7f + control.windTime, scl, mag) + movement.x * movescl,
                    Mathf.sin(index*7 - control.windTime - 5, scl*2f, mag) + movement.y * movescl);
                }

                index ++;
            });
        };

        if(facing == Dir.down){
            drawHair.run();
        }

        Draw.rect(region, cx, cy, cw, ch);

        if(facing != Dir.down){
            drawHair.run();
        }

        Draw.rect("hair-base" + facing.suffix, cx, cy, cw, ch);

        if(Core.atlas.isFound(eyes) && blinkTime <= 0f){
            Draw.color(0xff390dff);
            Draw.rect(eyes, cx, cy, cw, ch);
        }

        Draw.reset();
    }
}
