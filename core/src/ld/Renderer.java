package ld;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import ld.World.*;
import ld.entity.*;

import static ld.Game.*;

public class Renderer implements ApplicationListener{
    public FrameBuffer buffer = new FrameBuffer(2, 2);

    @Override
    public void update(){
        Core.graphics.clear(Color.black);

        Core.camera.position.set(player);
        Core.camera.position.add(0, (float)(Core.graphics.getHeight() % zoom) / zoom);

        Core.camera.update();
        Draw.proj(Core.camera);

        if(control.playing()){
            buffer.begin(Color.clear);
            draw();
            buffer.end();

            Draw.rect(buffer);
        }

        Draw.flush();
    }

    void drawTiles(){
        int padding = 2;
        Core.camera.bounds(Tmp.r1).grow(padding * tsize, padding * tsize * 2);
        int x1 = Math.max(world.t(Tmp.r1.x), 0), x2 = Math.min(world.t(Tmp.r1.x + Tmp.r1.width), world.width);
        int y1 = Math.max(world.t(Tmp.r1.y), 0), y2 = Math.min(world.t(Tmp.r1.y + Tmp.r1.height), world.height);

        for(int x = x1; x <= x2; x++){
            for(int y = y1; y <= y2; y++){
                Tile tile = world.tile(x, y);
                tile.floor.draw(x, y, 0);
            }
        }
    }

    void draw(){
        //tiles
        drawTiles();

        //TODO change order
        for(Entity e : control.entities){
            e.drawShadow();
        }

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
        Core.camera.resize(width / zoom, height / zoom);
        buffer.getTexture().setFilter(TextureFilter.Nearest);
        buffer.resize(width / zoom, height / zoom);
    }
}
