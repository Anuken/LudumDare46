package ld.gfx;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

import static ld.Game.*;

public class Snow{
    static Rand rand = new Rand();
    static final int amount = 110;
    static final int amountAdded = 140;
    static float time;

    public static void draw(){
        rand.setSeed(0);
        float speedScl = 1f + control.snowStrength * 1.5f;
        time += Time.delta() * speedScl;

        float yspeed = 2f, xspeed = 0.25f;
        float padding = 5f;
        float size = 8f;
        Core.camera.bounds(Tmp.r1);
        Tmp.r1.grow(padding);

        float total = amount + control.snowStrength*amountAdded;
        int rounded = Mathf.ceil(total);

        for(int i = 0; i < rounded; i++){
            float scl = rand.random(0.5f, 1f);
            float scl2 = rand.random(0.5f, 1f);
            float sscl = rand.random(0.4f, 1f);
            float x = (rand.random(-300, world.width * tsize + 300) + time * xspeed * scl2);
            float y = (rand.random(-300, world.height * tsize + 300) - time * yspeed * scl);

            if(i == rounded - 1){
                sscl *= total % 1f;
            }

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
