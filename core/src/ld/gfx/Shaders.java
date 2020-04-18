package ld.gfx;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;

import static ld.Game.renderer;

public class Shaders{
    public static final OutlineShader outline = new OutlineShader();
    public static final Shader light = new LoadShader("default2", "light"){
        @Override
        public void apply(){
            setUniformf("u_ambient", renderer.ambient);
        }
    };
    public static final Shader fire = new LoadShader("defaultbatch", "fire"){
        float[] values = new float[3 * 3];
        Color[] colors = {Pal.fire1, Pal.fire2, Pal.fire3};
        {
            for(int i = 0; i < colors.length; i++){
                values[i*3] = colors[i].r;
                values[i*3+1] = colors[i].g;
                values[i*3+2] = colors[i].b;
            }
        }

        @Override
        public void apply(){
            setUniformf("u_time", Time.time() / 60f);
            setUniform3fv("palette", values, 0, values.length);
        }
    };

    public static class OutlineShader extends LoadShader{
        public TextureRegion region;
        public Color color = Color.black.cpy();

        public OutlineShader(){
            super("defaultbatch", "outline");
        }

        @Override
        public void apply(){
            super.apply();

            setUniformf("u_step", Tmp.v1.set(1f/region.getWidth(), 1f/region.getHeight()));
            setUniformf("u_outline", color);
        }
    }

    public static class LoadShader extends Shader{

        public LoadShader(String vert, String frag){
            super(Core.files.internal("shaders/" + vert + ".vert"), Core.files.internal("shaders/" + frag + ".frag"));
        }
    }
}
