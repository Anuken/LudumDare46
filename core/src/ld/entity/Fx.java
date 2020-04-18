package ld.entity;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.gfx.*;

import static ld.Game.lFloor;

public class Fx{
    public static final Effect
    fireballs = new Effect(30f, e -> {
        Draw.z(e.y - 2f);
        Draw.color(Pal.fire3, Pal.fire1, e.fout());
        Angles.randLenVectors(e.id, 8, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
        });
    }),

    spark = new Effect(30, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Pal.fire3);
        Lines.stroke(2f * e.fout());
        Angles.randLenVectors(e.id, 2, 30f * e.fin(), (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Angles.angle(x, y), e.fslope() * 3f);
        });
    }),

    track = new Effect(100f, e -> {
        Draw.z(lFloor - 0.1f);
        Draw.alpha(e.fout(Interpolation.slowFast));
        Draw.color(0xcbdbfcff);
        Fill.square(e.x, e.y, 1f);
    }),

    breath = new Effect(90f, e -> {
        Draw.z(e.y - 13f);
        Draw.color(Color.white);
        Draw.alpha(e.fout() * 0.5f);
        Fill.circle(e.x, e.y, Mathf.randomSeed(e.id, 2f, 3f));
    }),

    pickup = new Effect(30, e -> {
        Draw.z(e.y + 10f);
        Draw.color(Color.white, Color.darkGray, e.fout());
        Angles.randLenVectors(e.id, 8, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
        });
    }),

    itemMove = new Effect(20, e -> {
        if(!(e.data instanceof ItemMove)) return;

        ItemMove item = e.data();
        Tmp.v1.set(e.x, e.y).lerp(item.target, e.fin(Interpolation.smooth));
        Draw.z(Tmp.v1.y - 8);
        Draw.rect(item.item.region(), Tmp.v1.x, Tmp.v1.y);
    });

    public static class ItemMove{
        public final Position target;
        public final Item item;

        public ItemMove(Position target, Item item){
            this.target = target;
            this.item = item;
        }
    }

}
