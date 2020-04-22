package ld;

import arc.*;
import arc.func.*;
import arc.fx.*;
import arc.fx.filters.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.util.*;
import ld.World.*;
import ld.entity.*;
import ld.gfx.*;

import static arc.Core.*;
import static ld.Game.*;

public class Renderer implements ApplicationListener{
    public FrameBuffer buffer = new FrameBuffer(2, 2);
    public FrameBuffer writeb = new FrameBuffer(2, 2);
    public FrameBuffer effects = new FrameBuffer(2, 2);
    public FrameBuffer lights = new FrameBuffer(2, 2);
    public FxProcessor fx = new FxProcessor();
    public Bloom bloom = new Bloom();
    public Color ambient = new Color(0.08f, 0.05f, 0.1f, 0);

    private float shakeIntensity, shaketime;

    @Override
    public void init(){

        fx.addEffect(new LevelsFilter(){
            @Override
            public void update(){
                this.saturation = Mathf.clamp(player.smoothHeat*6f);
                rebind();
            }
        });
    }

    @Override
    public void update(){
        Core.graphics.clear(Tmp.c1.set(0xd8dff7ff));
        Gl.clear(Gl.depthBufferBit);
        Draw.sort(false);

        Core.camera.position.set(player);
        updateShake(1f);
        Core.camera.position.add((float)(Core.graphics.getWidth() % zoom) / zoom, (float)(Core.graphics.getHeight() % zoom) / zoom);

        Core.camera.update();
        Draw.proj(Core.camera);

        ambient.a = control.lightness* 0.6f;

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
        }else{
            buffer.begin(Color.clear);
            Snow.draw();
            buffer.end();
        }

        ScreenRecorder.record();

        Draw.sort(false);
        Draw.flush();
    }

    public void shake(float intensity){
        shake(intensity, intensity);
    }

    public void shake(float intensity, float duration){
        shakeIntensity = Math.max(intensity, shakeIntensity);
        shaketime = Math.max(shaketime, duration);
    }

    public void jump(float angle, float intensity){
        camera.position.add(Tmp.v4.trns(angle, intensity));
    }

    void updateShake(float scale){
        if(shaketime > 0){
            float intensity = shakeIntensity * (settings.getInt("screenshake", 4) / 4f) * scale;
            camera.position.add(Mathf.range(intensity), Mathf.range(intensity));
            shakeIntensity -= 0.25f * Time.delta();
            shaketime -= Time.delta();
            shakeIntensity = Mathf.clamp(shakeIntensity, 0f, 100f);
        }else{
            shakeIntensity = 0f;
        }
    }

    public void drawNormal(Runnable run){
        Core.batch = defbatch;
        Draw.proj(Core.camera);
        effects.begin(Color.clear);
        run.run();
        effects.end();

        Core.batch = sbatch;
        Draw.rect(effects);
    }

    public void beginOutline(){
        Core.batch = defbatch;
        Draw.proj(Core.camera);
        effects.begin(Color.clear);
    }

    public void endOutline(){
        effects.end();

        Tmp.tr1.set(effects.getTexture());
        Shaders.outline.region = Tmp.tr1;

        writeb.begin(Color.clear);
        Draw.shader(Shaders.outline);
        Draw.rect(effects);
        Draw.shader();
        writeb.end();

        Core.batch = sbatch;
        Draw.rect(writeb);
    }

    void drawTiles(Intc2 drawer){
        int padding = 2;
        Core.camera.bounds(Tmp.r1).grow(padding * tsize, padding * tsize * 2);
        int x1 = world.t(Tmp.r1.x), x2 = world.t(Tmp.r1.x + Tmp.r1.width);
        int y1 = world.t(Tmp.r1.y), y2 = world.t(Tmp.r1.y + Tmp.r1.height);

        for(int x = x1; x <= x2; x++){
            for(int y = y1; y <= y2; y++){
                drawer.get(x, y);
            }
        }
    }

    void draw(){
        //assign matrix to queue light batch - must be done before flush
        Core.batch = qbatch;
        Draw.reset();
        Draw.proj(Core.camera);
        Core.batch = sbatch;
        Draw.reset();

        Draw.sort(false);
        //tiles - floor
        drawTiles((x, y) -> {
            Tile tile = world.tile(x, y);
            if(!tile.wall.solid){
                tile.floor.draw(x, y);
            }
        });

        //shadows
        effects.begin(Color.clear);
        Draw.sort(false); //shadows are unsorted

        drawTiles((x, y) -> {
            Tile tile = world.tile(x, y);
            tile.wall.drawShadow(x, y);
        });

        for(Entity e : control.entities){
            if(!camera.bounds(Tmp.r1).overlaps(Tmp.r2.setCentered(e.x, e.y, e.clipSize()))){
                continue;
            }

            e.drawShadow();
        }

        effects.end();
        Draw.color(shadowColor);
        Draw.rect(effects);
        Draw.color();

        Draw.sort(true);

        drawTiles((x, y) -> {
            Tile tile = world.tile(x, y);
            tile.wall.draw(x, y);
            if(tile.item != null){
                Draw.z(y*tsize + 2);
                Draw.rect(tile.item.region(), x * tsize, y * tsize);
            }
        });

        //entities
        for(Entity e : control.entities){
            if(!camera.bounds(Tmp.r1).overlaps(Tmp.r2.setCentered(e.x, e.y, e.clipSize()))){
                continue;
            }

            e.draw();
            Draw.reset();
        }

        Draw.sort(false);

        drawWeather();

        Draw.color();

        lights.begin(Color.clear);
        qbatch.flush();
        lights.end();

        //draw lights on top
        Draw.shader(Shaders.light);
        Draw.rect(lights);
        Draw.shader();
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
        writeb.resize(width, height);
        lights.resize(width, height);
        fx.resize(width, height);
        fx.setTextureFilter(TextureFilter.Nearest);

        bloom.dispose();
        bloom = new Bloom(width, height, true, true, true);
        bloom.setClearColor(0, 0, 0, 0);
    }
}
