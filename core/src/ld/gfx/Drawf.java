package ld.gfx;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import ld.*;

public class Drawf{

    public static void light(float x, float y, float rad){
        light(x, y, rad, Color.white);
    }

    public static void light(float x, float y, float rad, Color color){
        light(x, y, rad, color, 1f);
    }

    public static void light(float x, float y, float rad, Color color, float alpha){
        light(x, y, rad * 2, rad * 2, color, alpha);
    }

    public static void light(float x, float y, float width, float height, Color color, float alpha){
        Core.batch = Game.qbatch;
        Draw.color(color, alpha);
        Draw.rect("light", x, y, width, height);
        Draw.color();
        Core.batch = Game.sbatch;
    }

    public static void light(Color color, float alpha, Runnable run){
        Core.batch = Game.qbatch;
        Draw.color(color, alpha);
        run.run();
        Draw.color();
        Core.batch = Game.sbatch;
    }

    public static void sort(boolean sort){
        Game.sbatch.sort(sort);
    }

    public static void shadow(float x, float y, float size){
        Draw.rect("circle", x, y + 1, size * 2f + 1f, size * 1f);
    }
}
