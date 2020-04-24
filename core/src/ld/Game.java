package ld;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import ld.entity.*;
import ld.ui.*;

public class Game extends ApplicationCore{
    public static final int zoom = 3;
    public static final String infoText = "[orange]If your WASD controls don't work[], try switching tabs or refreshing the page.";
    public static final String about = "Made by [royal]Anuken[] for [yellow]LD46[] in 48 hours.";
    public static final String tutorial = "[orange]TUTORIAL[]\n\n- Use [orange]WASD[] to move.\n- Click to pick up objects.\n- Right-click to throw/drop objects.\n- Click two objects to craft them together.\n- Click to put wood in the fire.\n\n[orange]If your heat runs out, or if your fire dies, game over.";
    public static final boolean debug = false;
    public static final float tsize = 20;
    public static final Color shadowColor = new Color(0, 0, 0, 0.2f);
    public static final float speed = 2.7f;

    public static final float heatDuration = 60 * 60 * 2.5f, dayDuration = 60 * 60f * 3f, spawnGap = 60 * 6, spawnChance = 0.5f, maxEnemies = 200, teleportDur = 70f, fireDuration = 3f * 60f * 60f;
    public static final int worldSize = debug ? 200 : 200;

    public static BitmapFont font;
    public static SortedSpriteBatch sbatch;
    public static SpriteBatch defbatch;
    public static QueueBatch qbatch;

    public static Renderer renderer;
    public static UI ui;
    public static Control control;
    public static World world;

    public static Fire fire;
    public static Player player;

    @Override
    public void setup(){
        Core.settings.setAppName("ld46");
        Core.keybinds.setDefaults(Bind.values());
        Core.settings.load();

        defbatch = new SpriteBatch();
        qbatch = new QueueBatch();

        Core.camera = new Camera();
        Core.batch = sbatch = new SortedSpriteBatch();
        Core.atlas = new TextureAtlas("sprites/sprites.atlas");

        Draw.sortAscending(false);

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
