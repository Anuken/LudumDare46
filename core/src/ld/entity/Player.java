package ld.entity;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import ld.*;
import ld.World.*;
import ld.gfx.*;

import static ld.Game.*;

public class Player extends Entity{
    static int index = 0;
    public static final float height = 9f;
    static final float blinkDuration = 5f;
    static final float pickupRange = 50f, damageDur = 12f;

    public Dir dir = Dir.right;
    public Interval time = new Interval(4);

    public @Nullable Item item;
    public float heat = 1f, smoothHeat = heat, moveTime, hitTime, charge;

    Vec2 movement = new Vec2();
    boolean walking;
    float blinkTime;
    int tracki;

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
        Tmp.v1.setZero().set(Core.input.axis(Bind.move_x), Core.input.axis(Bind.move_y)).nor().scl(Game.speed - (item == null ? 0 : item.weight));

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

        //track effect
        if(time.get(0, 4f) && !Tmp.v1.isZero()){
            float s = 1.5f;
            Fx.track.at(x - s * Mathf.sign((tracki++ % 2) - 0.5f), y);
        }

        //animation
        if(!Tmp.v1.isZero()){
            moveTime += Time.delta();
        }

        walking = !Tmp.v1.isZero();

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
        if(Core.input.keyTap(Bind.interact)){
            Entity item = hovered();
            if(item != null){
                item.clicked();
            }else if(this.item != null && hoverTile().wall.interactable(this.item)){
                hoverTile().wall.clicked(world.t(Core.input.mouseWorldX()), world.t(Core.input.mouseWorldY()), this.item);
            }
        }

        if(Core.input.keyTap(Bind.drop) && item != null){
            if(!raycastClick()){
                //if something in the trajectory can interact with it...
                ItemEntity dropped = new ItemEntity(item);
                dropped.set(x, y + 5);
                dropped.velocity.trns(angle(), 6f);
                dropped.add();
                item = null;
            }
        }

        hitTime -= Time.delta() / damageDur;

        //hair fire
        if(Mathf.chance(0.4 * Time.delta() * smoothHeat)){
            Vec2 v = dir.direction;
            float scl = -5f, bs = -2f;
            float range = 1f;
            Fx.hairBurn.at(x + Mathf.range(range) + Mathf.random(v.x*scl) + v.x*bs, y + Mathf.range(range) + Mathf.random(v.y*scl) + 12 + v.y*bs);
        }
    }

    boolean raycastClick(){
        //origin
        Tmp.v1.trns(angle(), 16).add(x, y + height);
        //destination
        Tmp.v2.set(Core.input.mouseWorld()).sub(Tmp.v1).setLength(50).add(Tmp.v1);

        //query rect
        Tmp.r1.set(Math.min(Tmp.v1.x, Tmp.v2.x), Math.min(Tmp.v1.y, Tmp.v2.y), Math.abs(Tmp.v1.x - Tmp.v2.x), Math.abs(Tmp.v1.y - Tmp.v2.y));
        boolean[] clicked = {false};

        control.nearby(Tmp.r1).each(Entity::clickable, e -> {
            if(clicked[0]) return;
            e.hitbox(Tmp.r2);
            if(Intersector.intersectSegmentRectangle(Tmp.v1, Tmp.v2, Tmp.r2)){
                e.clicked();
                clicked[0] = true;
            }
        });

        return clicked[0];
    }

    public float angle(){
        return Angles.angle(x, y + 8, Core.input.mouseWorld().x, Core.input.mouseWorld().y);
    }

    @Nullable Entity hovered(){
        Entity entity = control.closest(Core.input.mouseWorld().x, Core.input.mouseWorld().y, 12f, e -> e.clickable());
        if(entity != null && !entity.within(player, pickupRange)){
            return null;
        }
        return entity;
    }

    Tile hoverTile(){
        return world.tilew(Core.input.mouseWorldX(), Core.input.mouseWorldY());
    }

    @Override
    public void draw(){
        Draw.z(y);

        Drawf.light(x, y + height, 100f * smoothHeat + 50f, Pal.fire2, smoothHeat/2f + 0.05f);

        renderer.beginOutline();
        drawDirection(dir);
        renderer.endOutline();
        Draw.reset();

        if(item != null && dir != Dir.up){
            float offset = 1f;
            Draw.rect(item.region(), x + dir.direction.x * offset, y + 6f + dir.direction.y * offset);
        }

        //draw input
        Draw.z(0);
        Entity item = hovered();
        if(item != null){
            float rad = 6f + Mathf.absin(4f, 2f);

            Lines.stroke(3f, Color.black);
            Lines.circle(item.x, item.y, rad);
            Lines.stroke(1f, Color.white);
            Lines.circle(item.x, item.y, rad);
        }else if(this.item != null && hoverTile().wall.interactable(this.item)){
            float rad = 7f + Mathf.absin(4f, 2f);

            float cx = world.t(Core.input.mouseWorldX()) * tsize, cy = world.t(Core.input.mouseWorldY()) * tsize;

            Lines.stroke(3f, Color.black);
            Lines.circle(cx, cy, rad);
            Lines.stroke(1f, Color.white);
            Lines.circle(cx, cy, rad);
        }
    }

    public boolean damagePeriodic(float amount){
        if(hitTime > 0) return false;

        heat -= amount / 100f;
        hitTime = 1f;
        return true;
    }

    void drawDirection(Dir dir){
        TextureRegion region = Core.atlas.find("player" + dir.suffix);
        TextureRegion hair = Core.atlas.find("hair" + dir.suffix);
        TextureRegion eyes = Core.atlas.find("eyes" + dir.suffix);
        TextureRegion hands = Core.atlas.find("hands" + dir.suffix);
        TextureRegion leg = Core.atlas.find("player-leg" + dir.suffix);

        Color hairColor = Tmp.c1.set(Color.white);
        float ha = heat * 0.6f;

        float cx = x, cy = y + region.getHeight()/2f, cw = region.getWidth() * (dir.flip ? -1 : 1), ch = region.getHeight();

        boolean moveHands = walking && charge <= 0f;
        float handRaise = 0;
        float amount = 2;
        float mscl = 30f;
        float base = (moveTime / mscl) % 1f;
        base = 1f - Math.abs(base - 0.5f) * 2f;

        float mov = walking ? base * amount - 0.25f : 0;
        if(dir.y){
            if(base < 0.5f || !walking) Draw.rect(leg, cx, cy);
            if(base >= 0.5f || !walking) Draw.rect(leg, cx - 4f, cy);
        }else{
            Draw.rect(leg, cx - mov, cy);
            Draw.rect(leg, cx - 4f + mov, cy);
        }

        Runnable drawHair = () -> {
            Draw.color(hairColor);

            Draw.rect(hair, cx, cy, cw, ch);

            index = 0;

            Draw.rectv(hair, cx, cy, cw, ch, v -> {
                float scl = 7f + index%2, mag = 2f, movescl = -2.6f;

                if((!dir.y && (index == 0 || index == 1)) || (dir.y && (index == 0 || index == 3))){
                    v.add(Mathf.sin(index*5.7f + control.windTime, scl, mag) + movement.x * movescl,
                    Mathf.sin(index*7 - control.windTime - 5, scl*2f, mag) + movement.y * movescl);
                }

                index ++;
            });

            Draw.reset();
        };

        if(dir == Dir.down){
            drawHair.run();
        }

        Draw.rect(region, cx, cy, cw, ch);

        if(dir != Dir.up){
            boolean cur = base < 0.5f;
            int scl = Mathf.num(moveHands) * (dir.y ? 1 : Mathf.sign(dir.direction.x));
            if(!dir.y){
                Draw.rect(base <= 0.5 && moveHands ? Core.atlas.find("handsh") : hands, cx - Mathf.num(base <= 0.5)*scl, cy + handRaise, cw, ch);
                Draw.rect(hands, cx - 6 * dir.direction.x - Mathf.num(base >= 0.5)*scl, cy + handRaise, cw, ch);
            }else{
                Draw.rect(hands, cx - Mathf.num(cur)*scl, cy + handRaise, cw, ch);
                Draw.rect(hands, cx - 7 + Mathf.num(!cur)*scl, cy + handRaise, cw, ch);
            }
        }

        if(dir != Dir.down){
            drawHair.run();
        }


        Draw.color(hairColor);
        Draw.rect("hair-base" + dir.suffix, cx, cy, cw, ch);
        Draw.reset();

        if(Core.atlas.isFound(eyes) && blinkTime <= 0f){
            //position
            Tmp.v2.set(x, y + 24);
            Vec2 o = Tmp.v1.set(charge > 0 ? Core.input.mouseWorld() : hovered() != null ? Tmp.v3.set(hovered()) : Tmp.v2).sub(Tmp.v2).limit(1f);

            if(this.dir.y){
                if(Math.abs(o.y) > 0.3f){
                    o.x = 0f;
                }
            }else{
                //looking down sideways look strange
                o.y = Math.max(o.y, 0f);
                if(Mathf.sign(o.x) == Mathf.sign(this.dir.direction.x)){
                    o.x = 0f;
                }
            }

            //looking up makes no sense in 3D space?
            //o.y = Math.min(o.y, 0f);

            Draw.color(Tmp.c1.set(0xff390dff));
            Draw.rect(eyes, cx + o.x, cy + o.y, cw, ch);
        }

        Draw.reset();
    }
}
