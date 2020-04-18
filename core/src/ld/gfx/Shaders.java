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
        @Override
        public void apply(){
            setUniformf("u_time", Time.time() / 60f);
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
