package ld.entity;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import ld.gfx.*;

import static ld.Game.renderer;

public class Fire extends Entity{
    public float heat;

    @Override
    public void draw(){
        Draw.z(y + 6);

        Draw.rect("fire-base", x, y);

        Draw.color(Color.red);
        Fill.circle(x, y, 3f);
        Draw.color();

        Draw.z(y - 1);

        renderer.drawNormal(() -> {
            Tmp.tr1.set(Core.atlas.texture());

            float width = 100, height = 100;

            Draw.shader(Shaders.fire);
            Draw.rect(Tmp.tr1, x, y + height * 0.43f, width, -height);
            Draw.shader();
        });
    }

    @Override
    public boolean solid(){
        return true;
    }
}
