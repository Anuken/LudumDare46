package ld;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.ui.*;
import ld.ui.*;

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
