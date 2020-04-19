package ld.entity;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.*;
import ld.gfx.*;

public class Fx{
    public static final Effect
    fireballs = new Effect(30f, e -> {
        Draw.z(e.y - 2f);
        Draw.color(Pal.fire3, Pal.fire1, e.fout());
        Angles.randLenVectors(e.id, 8, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
        });
    }),

    hairBurn = new Effect(40f, e -> {
        Draw.z(e.y - 10f);
        Draw.color(e.fin() > 0.5f ? Pal.fire1 : Pal.fire2);
        Angles.randLenVectors(e.id, 2, 20f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fslope() * 2f);
        });
    }),

    unlock = new Effect(50f, e -> {
        Draw.z(e.y);

        Draw.mixcol(Color.white, e.fout());
        Draw.alpha(e.fout());
        TextureRegion region = Core.atlas.find("door");
        Draw.rect(region, e.x, e.y + region.getWidth()/2f);
    }),

    fire = new Effect(90f, e -> {
        Draw.z(e.y + 2f);
        Draw.color(e.fin() > 0.5f ? Pal.fire1 : Pal.fire2);
        Angles.randLenVectors(e.id, 4, 60f * e.fin() * e.rotation, 90f, 40f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fslope() * 3f * e.rotation);
        });
    }),

    snowAttack = new Effect(30, e -> {
        Draw.z(e.y + 10f);

        Lines.stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, e.fin() * 20f);
    }),

    spark = new Effect(30, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Pal.fire3);
        Lines.stroke(2f * e.fout());
        Angles.randLenVectors(e.id, 2, 30f * e.fin(), (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Angles.angle(x, y), e.fslope() * 3f);
        });
    }),

    teleported = new Effect(35, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Pal.fire2);
        Lines.stroke(3f * e.fout());
        Angles.randLenVectors(e.id, 18, 40f * e.fin(), (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Angles.angle(x, y), e.fslope() * 5f);
        });
    }),

    track = new Effect(100f, e -> {
        Draw.z(e.x + 800f);
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
    }),

    itemThrow = new Effect(15, e -> {
        if(!(e.data instanceof ItemMove)) return;

        ItemMove item = e.data();
        Tmp.v1.set(e.x, e.y).lerp(item.target, e.fin(Interpolation.fastSlow));
        Draw.z(Tmp.v1.y - 8);
        Draw.rect(item.item.region(), Tmp.v1.x, Tmp.v1.y);
    }),

    chargeShot = new Effect(10, e -> {
        if(!(e.data instanceof Vec2)) return;

        Vec2 pos = e.data();
        Lines.stroke(e.fout() * 5f);
        Draw.color(Color.white, Pal.fire2, e.fout());
        Lines.line(e.x, e.y, (pos.x + e.x)/2f, (pos.y + e.y)/2f);
        Lines.stroke(e.fout() * 2f);
        Draw.color(Pal.fire3);
        Lines.line(e.x, e.y, pos.x, pos.y);

        Angles.randLenVectors(e.id, 8, 50f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
        });
    }),

    hitsnow = new Effect(15, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Color.white, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 7, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 1f);
        });
    }),

    chop = new Effect(25f, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Pal.woodLight, Pal.woodDark, e.fin() <= 0.5f ? 1f : 0f);
        Angles.randLenVectors(e.id, 7, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 1f);
        });
    }),

    mine = new Effect(25f, e -> {
        Draw.z(e.y - 15f);
        Draw.color(0x605682ff);
        Angles.randLenVectors(e.id, 7, 30f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 1f);
        });
    }),

    deathsnow = new Effect(20, e -> {
        Draw.z(e.y - 10f);
        Draw.color(Color.white, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 20, 50f * e.fin(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 1f);
        });
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
