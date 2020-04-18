package ld;

import arc.graphics.g2d.*;

public class Drawf{

    public static void shadow(float x, float y, float size){
        Draw.rect("circle", x, y + 1, size * 2f + 1f, size * 1f);
    }
}
