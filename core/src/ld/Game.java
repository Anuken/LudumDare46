package ld;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import ld.entity.*;
import ld.ui.*;

public class Game extends ApplicationCore{
    public static final int zoom = 3;
    public static final String about = "Made by [royal]Anuken[] for [yellow]LD46[].\nTools Used:\n";
    public static final boolean debug = true;
    public static final float tsize = 12;
    public static final Color shadowColor = new Color(0, 0, 0, 0.2f);

    public static BitmapFont font;

    public static Renderer renderer;
    public static UI ui;
    public static Control control;
    public static World world;

    public static Player player;
    public static float speed = 3f;

    @Override
    public void setup(){
        Core.settings.setAppName("ld46");
        Core.keybinds.setDefaults(Bind.values());
        Core.settings.load();

        Core.camera = new Camera();
        Core.batch = new SpriteBatch();
        Core.atlas = new TextureAtlas("sprites/sprites.atlas");

        font = new BitmapFont(Core.files.internal("prose.fnt"));
        font.getData().markupEnabled = true;

        Core.scene = new Scene();
        Core.atlas.setDrawableScale(4f);
        Tex.load();
        Tex.register();
        Core.input.addProcessor(Core.scene);

        world = new World();

        add(control = new Control());
        add(renderer = new Renderer());
        add(ui = new UI());
    }


}
