package ld.entity;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;

import static ld.Game.lFloor;

public class Fx{
    public static final Effect
    fire = new Effect(40f, e -> {

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
    });

}
