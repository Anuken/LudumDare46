package ld.entity;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

import static ld.Game.*;

public class Snow{
    static Rand rand = new Rand();
    static final int amount = 200;

    public static void draw(){
        rand.setSeed(0);
        float yspeed = 2f, xspeed = 0.25f;
        float padding = 5f;
        float size = 10f;
        Core.camera.bounds(Tmp.r1);
        Tmp.r1.grow(padding);

        for(int i = 0; i < amount; i++){
            float scl = rand.random(0.5f, 1f);
            float scl2 = rand.random(0.5f, 1f);
            float sscl = rand.random(0.2f, 1f);
            float x = (rand.random(0f, world.width * tsize) + Time.time() * xspeed * scl2);
            float y = (rand.random(0f, world.height * tsize) - Time.time() * yspeed * scl);

            x += Mathf.sin(y, rand.random(30f, 80f), rand.random(1f, 7f));

            x -= Tmp.r1.x;
            y -= Tmp.r1.y;
            x = Mathf.mod(x, Tmp.r1.width);
            y = Mathf.mod(y, Tmp.r1.height);
            x += Tmp.r1.x;
            y += Tmp.r1.y;

            Draw.rect("circle", x, y, size * sscl, size * sscl);
        }
    }
}
