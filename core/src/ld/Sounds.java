package ld;

import arc.*;
import arc.audio.*;

public enum Sounds{
    ;

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

    public void play(float x, float y){
        checkInit();

        sound.at(x, y);
    }

    public float volume(){
        return Core.settings.getInt("sfxvol", 100) / 100f;
    }
}
