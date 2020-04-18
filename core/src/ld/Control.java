package ld;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.ArcAnnotate.*;
import arc.util.noise.*;
import ld.entity.*;

import static ld.Game.*;

public class Control implements ApplicationListener{
    State state = State.menu;

    Array<Entity> entities = new Array<>(), removal = new Array<>(), addition = new Array<>();
    QuadTree<Entity> quadtree;
    Array<Entity> out = new Array<>();

    public float windTime;

    public float windStrength(){
        return Noise.nnoise(Time.time(), 0f, 50f, 1f);
    }

    public @Nullable Entity closest(float x, float y, float radius, Boolf<Entity> test){
        out.clear();
        quadtree.getIntersect(out, Tmp.r1.setCentered(x, y, radius * 2f));
        out.sort(e -> test.get(e) ? Float.POSITIVE_INFINITY : e.dst2(x, y));
        return out.first() != null && test.get(out.first()) ? out.first() : null;
    }

    public Array<Entity> nearby(float x, float y, float radius){
        out.clear();
        quadtree.getIntersect(out, Tmp.r1.setCentered(x, y, radius * 2f));
        return out;
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
        quadtree = new QuadTree<>(new Rect(0, 0, world.uwidth(), world.uheight()));

        player.add();
        player.set(world.uwidth()/2f, world.uheight()/2f);

        Fire fire = new Fire();
        fire.heat = 1f;
        fire.set(player.x, player.y);
        fire.add();
    }

    public void process(){
        quadtree.clear();
        for(Entity e : entities){
            if(e.solid()){
                quadtree.insert(e);
            }
        }

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
