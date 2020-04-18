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
    public static final float tsize = 20;
    public static final Color shadowColor = new Color(0, 0, 0, 0.2f);

    public static final float lFloor = 20f * 105f, lWeather = -20f;
    public static final float heatDuration = 60 * 60 * 2;

    public static BitmapFont font;
    public static SortedSpriteBatch sbatch;
    public static SpriteBatch defbatch;
    public static QueueBatch qbatch;

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

        /*
        ZSpriteBatch batch = new ZSpriteBatch();
        batch.maxZ = 20f * 110f;
        batch.minZ = -40f;
        batch.flipZ = true;*/

        defbatch = new SpriteBatch();
        qbatch = new QueueBatch();

        Core.camera = new Camera();
        Core.batch = sbatch = new SortedSpriteBatch();
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
