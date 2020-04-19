package ld.entity;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.*;
import ld.entity.Fx.*;

import static ld.Game.player;

public class ItemEntity extends Entity{
    public Item item;
    public Vec2 velocity = new Vec2();

    public ItemEntity(Item item){
        this.item = item;
    }

    @Override
    public boolean canTeleport(){
        return true;
    }

    @Override
    public boolean clickable(){
        return player.item == null || Recipe.has(player.item, item);
    }

    @Override
    public void update(){
        updateTeleport();
        super.update();

        if(!velocity.isZero()){
            move(velocity.x, velocity.y);

            float drag = 0.13f;

            velocity.scl(Mathf.clamp(1f - drag * Time.delta()));
        }
    }

    @Override
    public void clicked(){
        if(player.item != null && Recipe.has(player.item, item)){
            Fx.itemMove.at(player.x, player.y + 6, 0, new ItemMove(this, player.item));

            Time.run(Fx.pickup.lifetime*0.75f, () -> Fx.pickup.at(this));

            //craft
            Item result = Recipe.get(player.item, item).result;
            Time.run(Fx.pickup.lifetime, () -> {
                this.item = result;
            });

            player.item = null;
        }else{
            remove();

            Fx.pickup.at(this);
            Fx.itemMove.at(x, y, 0, new ItemMove(new Position(){
                @Override
                public float getX(){
                    return player.x;
                }

                @Override
                public float getY(){
                    return player.y + 6;
                }
            }, item));
            Time.run(Fx.itemMove.lifetime, () -> {
                if(player.item != null){
                    create(item, player.x, player.y + 4f);
                }else{
                    player.item = item;
                }
            });
        }


    }

    @Override
    public boolean solid(){
        return true;
    }

    @Override
    public void draw(){
        super.draw();
        Draw.z(y + 2);
        Draw.rect(item.region(), x, y);
    }

    @Override
    public void drawShadow(){
        //Drawf.shadow(x, y, 6f);
    }

    public static ItemEntity create(Item item, float x, float y){
        ItemEntity e = new ItemEntity(item);
        e.set(x, y);
        e.add();
        return e;
    }
}
