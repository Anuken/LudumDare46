package ld;

import arc.*;
import arc.util.*;

import static ld.Game.debug;

public class Control implements ApplicationListener{
    State state = State.menu;

    @Override
    public void init(){
        reset();
        if(debug){
            state = State.playing;
        }
    }

    @Override
    public void update(){
        Time.updateGlobal();

        if(playing()){

            if(paused()){
                Time.update();
            }

        }
    }

    public void play(){
        reset();
        state = State.playing;
    }

    public void reset(){
        state = State.menu;
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
