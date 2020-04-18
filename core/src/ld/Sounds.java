package ld;

import arc.*;
import arc.audio.*;

public enum Sounds{
    ;
    private Sound sound;

    Sounds(){
        sound = Core.audio.newSound(Core.files.internal("sounds/" + name() + ".ogg").exists() ? Core.files.internal("sounds/" + name() + ".ogg") : Core.files.internal("sounds/" + name() + ".wav"));
    }

    public void play(){
        sound.play();
    }

    public void play(float x, float y){
        sound.at(x, y);
    }

    public float volume(){
        return Core.settings.getInt("sfxvol", 100) / 100f;
    }
}
