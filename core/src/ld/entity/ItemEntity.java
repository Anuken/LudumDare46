package ld.entity;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import ld.entity.Fx.*;

import static ld.Game.player;

public class ItemEntity extends SelectableEntity{
    public Item item;

    public ItemEntity(Item item){
        this.item = item;
    }

    @Override
    public boolean clickable(){
        return player.item == null;
    }

    @Override
    public void clicked(){
        remove();
        Fx.pickup.at(this);
        Fx.itemMove.at(x, y, 0, new ItemMove(new Vec2(player.x, player.y + 6), item));
        Time.run(Fx.itemMove.lifetime, () -> {
            player.item = item;
        });
    }

    @Override
    public boolean solid(){
        return true;
    }

    @Override
    public void draw(){
        Draw.z(y + 2);
        Draw.rect(item.region, x, y);
    }

    @Override
    public void drawShadow(){
        //Drawf.shadow(x, y, 6f);
    }
}
