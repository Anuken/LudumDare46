package ld;

import arc.*;
import arc.input.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import ld.entity.*;

import static ld.Game.*;

public class Control implements ApplicationListener{
    State state = State.menu;

    Array<Entity> entities = new Array<>(), removal = new Array<>(), addition = new Array<>();

    public float windTime;

    public float windStrength(){
        return Noise.nnoise(Time.time(), 0f, 50f, 1f);
    }

    public void reset(){
        state = State.menu;
        player = new Player();
        entities.clear();
        removal.clear();
        addition.clear();
    }

    public void play(){
        reset();
        state = State.playing;

        world.resize(100, 100);
        player.add();
        player.set(world.uwidth()/2f, world.uheight()/2f);
    }

    public void process(){
        for(Entity e : entities){
            e.update();
        }

        entities.addAll(addition);
        entities.removeAll(removal);

        addition.clear();
        removal.clear();
    }

    public void add(Entity entity){
        addition.add(entity);
    }

    public void remove(Entity entity){
        removal.add(entity);
    }

    @Override
    public void init(){
        if(debug){
            play();
        }else{
            reset();
        }
    }

    @Override
    public void update(){
        Time.updateGlobal();

        if(state == State.playing){
            Time.update();
            windTime += windStrength() * Time.delta();

            process();
        }

        if(debug){
            if(Core.input.keyTap(KeyCode.escape)){
                Core.app.exit();
            }
        }
    }

    public void set(State state){
        this.state = state;
    }

    public boolean paused(){
        return state == State.paused;
    }

    public boolean playing(){
        return !menu();
    }

    public boolean menu(){
        return state == State.menu;
    }

    public enum State{
        playing, paused, menu;
    }
}
