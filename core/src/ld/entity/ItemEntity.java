package ld.entity;

import arc.graphics.g2d.*;
import ld.gfx.*;

public class ItemEntity extends Entity{
    public Item item;

    public ItemEntity(Item item){
        this.item = item;
    }

    @Override
    public void draw(){
        Draw.z(y);
        Draw.rect(item.region, x, y + item.region.getHeight()/2f);
    }

    @Override
    public void drawShadow(){
        Drawf.shadow(x, y, 6f);
    }
}
