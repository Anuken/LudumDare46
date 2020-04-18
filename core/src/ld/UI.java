package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.util.*;
import ld.ui.*;

import static ld.Game.player;

public class UI implements ApplicationListener{
    public Dialog paused, about;
    public SettingsDialog settings;
    public KeybindDialog keys;

    @Override
    public void init(){
        settings = new SettingsDialog();
        keys = new KeybindDialog();

        DefaultDialog.addCloseButton(settings);
        DefaultDialog.addCloseButton(keys);

        about = new DefaultDialog("About");
        about.cont.add(Game.about);

        paused = new Dialog("Paused");

        //menu
        Core.scene.table(t -> {
            t.visible(Game.control::menu);
            t.center();

            t.table(c -> {
                c.defaults().size(200f, 50f);

                c.button("Play", Game.control::play).row();
                c.button("About", about::show).row();
                c.button("Settings", settings::show).row();
                c.button("Controls", keys::show);
            });
        });

        //playing stuff
        Core.scene.table(t -> {
            t.visible(Game.control::playing);

            t.top().table(Tex.button, bar -> {
                bar.rect((x, y, w, h) -> {
                    float fract = player.smoothHeat;
                    Draw.color(0x0e0d11ff);
                    Fill.crect(x, y, w, h);
                    Draw.color(Tmp.c1.set(0xff9940ff), Tmp.c2.set(0xfcd357ff), Mathf.absin(Time.time(), 20f, 1f));
                    Fill.crect(x, y, Mathf.round(w * fract, 4), h);
                    Draw.color();
                }).grow();
            }).size(300f, 30f);
        });
    }

    @Override
    public void update(){

        Core.scene.act();
        Core.scene.draw();

        Draw.flush();
    }

    @Override
    public void resize(int width, int height){
        Core.scene.resize(width, height);
    }
}
