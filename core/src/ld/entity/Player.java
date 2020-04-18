package ld.entity;

import arc.*;
import arc.graphics.*;
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
    static final float pickupRange = 50f;

    public Dir dir = Dir.right;
    public Interval time = new Interval(4);

    public @Nullable Item item;
    public float heat = 1f, smoothHeat = heat, moveTime;

    Vec2 movement = new Vec2();
    float blinkTime;

    @Override
    public boolean solid(){
        return true;
    }

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y + 1, 8f);
    }

    @Override
    public void update(){

        //movement
        Tmp.v1.setZero().set(Core.input.axis(Bind.move_x), Core.input.axis(Bind.move_y)).nor().scl(Game.speed);

        move(Tmp.v1.x, Tmp.v1.y);

        //assign direction
        if(Tmp.v1.x > 0.0001f){
            dir = Dir.right;
        }else if(Tmp.v1.x < -0.0001f){
            dir = Dir.left;
        }else if(Tmp.v1.y < -0.0001f){
            dir = Dir.down;
        }else if(Tmp.v1.y > 0.0001f){
            dir = Dir.up;
        }

        //breath
        if(time.get(0, 2f) && !Tmp.v1.isZero()){
            float s = 1;
            Fx.track.at(x - s, y);
            Fx.track.at(x + s, y);
        }

        //animation
        if(!Tmp.v1.isZero()){
            moveTime += Time.delta();
        }

        //blinking
        blinkTime -= Time.delta();

        if(time.get(2, 60f * 2.9f) && Mathf.chance(0.6)){
            blinkTime = blinkDuration;
        }

        //breathing
        if(time.get(1, 90f) && Mathf.chance(0.3)){
            float rand = 2f, scl = 4f;
            Fx.breath.at(x + dir.direction.x*scl + Mathf.range(rand), y + dir.direction.y*scl + Mathf.range(rand) + 11f);
        }

        movement.lerpDelta(Tmp.v1.nor(), 0.14f);

        //passive heat decay
        heat -= (1f + control.windStrength()) / heatDuration;

        //recharge of heat from nearby fires
        Fire fire = control.closest(x, y, 30f, e -> e instanceof Fire);
        if(fire != null){
            heat = 1f;//Math.max(heat, fire.heat);
        }

        heat = Math.max(heat, 0f);
        smoothHeat = Mathf.lerpDelta(smoothHeat, heat, 0.05f);

        //pick up nearby objects
        if(Core.input.keyTap(Bind.pickup)){
            SelectableEntity item = hovered();
            if(item != null){
                item.clicked();
            }
        }
    }

    @Nullable SelectableEntity hovered(){
        SelectableEntity entity = control.closest(Core.input.mouseWorld().x, Core.input.mouseWorld().y, 12f, e -> e instanceof SelectableEntity && ((SelectableEntity)e).clickable());
        if(entity != null && !entity.within(player, pickupRange)){
            return null;
        }
        return entity;
    }

    @Override
    public void draw(){
        Draw.z(y);

        renderer.beginOutline();
        drawDirection(dir);
        renderer.endOutline();
        Draw.reset();

        if(item != null && dir != Dir.up){
            float offset = 1f;
            Draw.rect(item.region, x + dir.direction.x * offset, y + 6f + dir.direction.y * offset);
        }

        //draw input
        Draw.z(0);
        SelectableEntity item = hovered();
        if(item != null){
            float rad = 6f + Mathf.absin(4f, 2f);

            Lines.stroke(3f, Color.black);
            Lines.circle(item.x, item.y, rad);
            Lines.stroke(1f, Color.white);
            Lines.circle(item.x, item.y, rad);
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
                float scl = 7f + index%2, mag = 2f, movescl = -2.6f;

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
            //position
            Tmp.v2.set(x, y + 24);
            Vec2 o = Tmp.v1.set(hovered() != null ? hovered() : Tmp.v2).sub(Tmp.v2).limit(1f);

            if(dir.y){
                if(Math.abs(o.y) > 0.3f){
                    o.x = 0f;
                }
            }else{
                //looking down sideways look strange
                o.y = Math.max(o.y, 0f);
            }

            //looking up makes no sense in 3D space?
            o.y = Math.min(o.y, 0f);

            Draw.color(0xff390dff);
            Draw.rect(eyes, cx + o.x, cy + o.y, cw, ch);
        }

        Draw.reset();
    }
}
