package ld.entity;

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
    });

}
