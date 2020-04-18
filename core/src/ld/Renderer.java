package ld;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;

import static ld.Game.*;

public class Renderer implements ApplicationListener{

    @Override
    public void update(){
        Core.graphics.clear(Color.black);

        Core.camera.update();
        Draw.proj(Core.camera);

        if(control.playing()){
            draw();
        }

        Draw.flush();
    }

    void draw(){

    }

    @Override
    public void resize(int width, int height){
        Core.camera.resize(width / zoom, height / zoom);
    }
}
