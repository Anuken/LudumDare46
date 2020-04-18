package ld.entity;

import arc.math.geom.*;

public enum Dir{
    left(true, "", -1, 0),
    right(false, "", 1, 0),
    up(false, "-back", 0, 1),
    down(false, "-front", 0, -1);

    public final boolean flip, y;
    public final String suffix;
    public final Vec2 direction;

    Dir(boolean flip, String suffix, float dx, float dy){
        this.flip = flip;
        this.suffix = suffix;
        this.direction = new Vec2(dx, dy);
        y = Math.abs(dy) > 0.01;
    }
}
