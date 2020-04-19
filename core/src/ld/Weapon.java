package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.entity.*;
import ld.gfx.*;

import static ld.Game.*;

public enum Weapon{
    beam{
        public void draw(Player player, float fract){
            float cx = player.x, cy = player.y + Player.height, len = 16f;
            float sfract = 0.5f;
            float angle = player.angle();

            Lines.stroke(2f * fract, Pal.fire2);
            Lines.swirl(cx, cy, len, sfract, -sfract*360f/2f + angle);

            Lines.stroke(3.5f * fract, Pal.fire2);
            Lines.swirl(cx, cy, len, sfract/2f, -sfract*360f/2f/2f + angle);

            Lines.stroke(1f * fract, Pal.fire3);
            Lines.swirl(cx, cy, len, sfract/2f, -sfract*360f/2f/2f + angle);

            Tmp.v1.trns(angle, len);

            Draw.color(Pal.fire2, Pal.fire1, Mathf.absin(3f, 1f));

            Angles.randLenVectors(0, 20, (1f - fract) * 80f, (x, y) -> {
                Fill.poly(cx + Tmp.v1.x + x, cy + Tmp.v1.y + y, 4, 5f * fract, angle);
            });

            Lines.stroke(fract * 1f);
            Lines.lineAngleCenter(cx + Tmp.v1.x, cy + Tmp.v1.y, angle + 90f, 50f * fract);

            Lines.stroke(fract * 2f);
            Lines.lineAngleCenter(cx + Tmp.v1.x, cy + Tmp.v1.y, angle + 90f, 20f * fract);

            Draw.reset();
        }

        public void shoot(Player player, float fract){
            float damage = fract * 30f;
            player.heat -= fract * 0.01f;

            //origin
            Tmp.v1.trns(player.angle(), 16).add(player.x, player.y + Player.height);
            //destination
            Tmp.v2.set(Core.input.mouseWorld()).sub(Tmp.v1).setLength(300).add(Tmp.v1);

            //query rect
            Tmp.r1.set(Math.min(Tmp.v1.x, Tmp.v2.x), Math.min(Tmp.v1.y, Tmp.v2.y), Math.abs(Tmp.v1.x - Tmp.v2.x), Math.abs(Tmp.v1.y - Tmp.v2.y));
            control.nearby(Tmp.r1).each(e -> e instanceof Enemy, e -> {
                Enemy n = (Enemy)e;
                e.hitbox(Tmp.r2);
                if(Intersector.intersectSegmentRectangle(Tmp.v1, Tmp.v2, Tmp.r2)){
                    n.damage(damage);
                    n.move(Tmp.v3.trns(player.angle(), 10f * fract));
                }
            });

            //effects
            Fx.chargeShot.at(Tmp.v1.x, Tmp.v1.y, 0, Tmp.v2.cpy());
            renderer.shake(5f);
        }
    };

    public void draw(Player player, float fract){
    }

    public void shoot(Player player, float fract){
    }
}
