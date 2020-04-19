package ld;

import arc.*;
import arc.audio.*;
import arc.math.geom.*;

public enum Sounds{
    craft, gameover, pickup, hit, hitSnow, teleport, die, place, chip, breakBlock, throwItem, burnItem;

    private Sound sound;

    Sounds(){

    }

    private void checkInit(){
        if(sound == null){
            sound = Core.audio.newSound(Core.files.internal("sounds/" + name() + ".ogg").exists() ? Core.files.internal("sounds/" + name() + ".ogg") : Core.files.internal("sounds/" + name() + ".wav"));
        }
    }

    public void play(){
        checkInit();

        sound.play();
    }

    public void play(Position pos){
        play(pos.getX(), pos.getY());
    }

    public void play(float x, float y){
        checkInit();

        sound.at(x, y);
    }

    public float volume(){
        return Core.settings.getInt("sfxvol", 100) / 100f;
    }
}
