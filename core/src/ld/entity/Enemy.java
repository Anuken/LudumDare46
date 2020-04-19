package ld.entity;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import ld.entity.Fx.*;
import ld.gfx.*;

import static ld.Game.fire;
import static ld.Game.*;

public class Enemy extends Entity{
    public static int count;

    public float health, hitTime, size;
    public static final Color hitColor = Color.white;

    private Interval timer = new Interval(4);

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

    public Array<Drop> drops(){
        return Array.with();
    }

    @Override
    public boolean clickable(){
        return player.item != null && player.item.damage > 0;
    }

    @Override
    public void clicked(){
        Item item = player.item;

        if(!item.thrown){
            if(timer.get(1, item.reload)){
                damage(item.damage);
                player.attackTime = 1f;
            }
        }else{
            Fx.itemThrow.at(player.x, player.y + 6, 0, new ItemMove(top(), player.item));
            Time.run(Fx.itemThrow.lifetime * 0.9f, () -> damage(item.damage));
            if(!item.consumed){
                Time.runTask(Fx.itemThrow.lifetime, () -> {
                    ItemEntity e = new ItemEntity(item);
                    e.set(x, y + 5);
                    e.add();
                });
            }
            player.item = null;
        }
    }

    @Override
    public void add(){
        if(!added) count ++;
        super.add();
    }

    @Override
    public void remove(){
        if(added) count --;
        super.remove();
    }

    @Override
    public void update(){
        super.update();

        hitTime -= Time.delta() / 20f;
        hitTime = Mathf.clamp(hitTime);
        size = Mathf.lerpDelta(size, 1f, 0.03f);

        if(!within(player, 700)){
            remove();
        }

        if(within(fire, 12f) && timer.get(0, 15f)){
            damage(9f);
            fire.heat -= 0.05f;
        }
    }

    @Override
    public boolean solid(){
        return true;
    }

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y, 10f * size);
    }

    public void killed(){
        for(Drop drop : drops()){
            if(Mathf.chance(drop.chance)){
                ItemEntity.create(drop.item, x, y + 5).velocity.rnd(1f);
            }
        }
    }

    public Effect hitEffect(){
        return Fx.hitsnow;
    }

    public Effect deathEffect(){
        return Fx.deathsnow;
    }

    public static class Drop{
        public final Item item;
        public final double chance;

        public Drop(Item item, double chance){
            this.item = item;
            this.chance = chance;
        }
    }
}
