package ld;

import arc.*;
import arc.func.*;
import arc.fx.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import ld.World.*;
import ld.entity.*;
import ld.gfx.*;

import static ld.Game.*;

public class Renderer implements ApplicationListener{
    public FrameBuffer buffer = new FrameBuffer(2, 2, true);
    public FrameBuffer effects = new FrameBuffer(2, 2, true);
    public FxProcessor fx = new FxProcessor();
    public Bloom bloom = new Bloom();


    @Override
    public void init(){
        /*
        fx.addEffect(new LevelsFilter(){
            @Override
            public void update(){
                this.saturation = Mathf.absin(20f, 1f);
                rebind();
            }
        });*/
    }

    @Override
    public void update(){
        Core.graphics.clear(Color.black);
        Gl.clear(Gl.depthBufferBit);

        Core.camera.position.set(player);
        Core.camera.position.add(0, (float)(Core.graphics.getHeight() % zoom) / zoom);

        Core.camera.update();
        Draw.proj(Core.camera);

        if(control.playing()){
            buffer.begin(Color.clear);
            draw();
            buffer.end();

            fx.clear(Color.clear);
            fx.begin();
            Draw.rect(buffer);
            fx.end();
            fx.applyEffects();
            fx.render();
        }

        ScreenRecorder.record();

        Draw.flush();
    }

    public void beginOutline(){
        effects.begin(Color.clear);
    }

    public void endOutline(){
        effects.end();

        Tmp.tr1.set(effects.getTexture());
        Shaders.outline.region = Tmp.tr1;
        Draw.shader(Shaders.outline);
        Draw.rect(effects);
        Draw.shader();
    }

    void drawTiles(Intc2 drawer){
        int padding = 2;
        Core.camera.bounds(Tmp.r1).grow(padding * tsize, padding * tsize * 2);
        int x1 = Math.max(world.t(Tmp.r1.x), 0), x2 = Math.min(world.t(Tmp.r1.x + Tmp.r1.width), world.width);
        int y1 = Math.max(world.t(Tmp.r1.y), 0), y2 = Math.min(world.t(Tmp.r1.y + Tmp.r1.height), world.height);

        for(int x = x1; x <= x2; x++){
            for(int y = y1; y <= y2; y++){
                drawer.get(x, y);
            }
        }
    }

    void draw(){
        //tiles
        drawTiles((x, y) -> {
            Tile tile = world.tile(x, y);
            tile.floor.draw(x, y);
            tile.wall.draw(x, y);
        });

        effects.begin(Color.clear);

        drawTiles((x, y) -> {
            Tile tile = world.tile(x, y);
            if(tile.wall.solid){
                Draw.rect("wallshadow", x * tsize, y * tsize);
            }
        });

        //TODO change order
        for(Entity e : control.entities){
            e.drawShadow();
        }

        effects.end();

        Draw.color(shadowColor);
        Draw.rect(effects);
        Draw.color();

        //entities
        for(Entity e : control.entities){
            e.draw();
        }

        drawWeather();
    }

    void drawWeather(){
        Snow.draw();
    }

    @Override
    public void resize(int width, int height){
        width /= zoom;
        height /= zoom;

        Core.camera.resize(width, height);
        buffer.getTexture().setFilter(TextureFilter.Nearest);
        effects.getTexture().setFilter(TextureFilter.Nearest);
        buffer.resize(width, height);
        effects.resize(width, height);
        fx.resize(width, height);
        fx.setTextureFilter(TextureFilter.Nearest);

        bloom.dispose();
        bloom = new Bloom(width, height, true, true, true);
    }
}
