package ld;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.ArcAnnotate.*;
import arc.util.*;
import arc.util.noise.*;
import ld.World.*;
import ld.entity.*;
import ld.ui.*;

import static ld.Game.*;

public class Control implements ApplicationListener{
    static final float[] dayValues = {1f, 1f, 1f, 1f, 0f, 0f, 0f, 1f};


    static final Array<Prov<Enemy>> enemies = Array.with(
    SnowDemon::new
    );

    State state = State.menu;

    Array<Entity> entities = new Array<>(), removal = new Array<>(), addition = new Array<>();
    QuadTree<Entity> quadtree;
    Array<Entity> out = new Array<>();

    public float windTime;

    public float lightness = 1f;
    public boolean gameover = false;

    private float dayTime;
    private Interval times = new Interval(10);

    public float windStrength(){
        return Noise.nnoise(Time.time(), 0f, 50f, 1f);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends Entity> T closest(float x, float y, float radius, Boolf<Entity> test){
        out.clear();
        quadtree.getIntersect(out, Tmp.r1.setCentered(x, y, radius * 2f));
        out.sort(e -> !test.get(e) ? Float.POSITIVE_INFINITY : e.dst2(x, y));
        return out.any() && test.get(out.first()) ? (T)out.first() : null;
    }

    public Array<Entity> nearby(Rect rect){
        out.clear();
        quadtree.getIntersect(out, rect);
        return out;
    }

    public Array<Entity> nearby(float x, float y, float radius){
        out.clear();
        quadtree.getIntersect(out, Tmp.r1.setCentered(x, y, radius * 2f));
        return out;
    }

    public void reset(){
        windTime = dayTime = 0f;
        lightness = 1f;
        state = State.menu;
        player = new Player();
        entities.clear();
        removal.clear();
        addition.clear();
        gameover = false;
    }

    public void play(){
        reset();

        ui.showLoading(() -> {
            state = State.playing;

            int size = worldSize;

            world.resize(size, size);
            quadtree = new QuadTree<>(new Rect(0, 0, world.uwidth(), world.uheight()));

            Geometry.circle(size/2, size/2, 10, (x, y) -> world.tile(x, y).wall = Block.none);

            player.add();
            player.set(world.uwidth()/2f, world.uheight()/2f - 20f);

            fire = new Fire();
            fire.heat = 1f;
            fire.set(world.uwidth()/2f, world.uheight()/2f);
            fire.add();

            for(int i = 0; i < 30; i++){
                ItemEntity item = new ItemEntity(Item.stick);
                item.set(player.x + Mathf.range(100f), player.y + Mathf.range(100f));
                //item.add();
            }

            if(debug){
                SnowDemon demon = new SnowDemon();
                demon.set(player.x + 30f, player.y);
                demon.add();

                world.tile(size/2, size/2 - 9).item = Item.axe;
                world.tile(size/2, size/2 - 5).floor = Block.teleporter;
            }
        });
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

            checkItem();

            spawnEnemies();

            process();
            dayTime += Time.delta();
            dayTime %= dayDuration;

            float fract = dayTime / dayDuration;

            int index = Math.min((int)(fract * dayValues.length), dayValues.length - 1);
            int nextIndex = (index + 1) % dayValues.length;
            float interValue = (fract * dayValues.length - index);
            lightness = 1f - Mathf.lerp(dayValues[index], dayValues[nextIndex], interValue);

            if(Core.input.keyTap(KeyCode.escape)){
                state = State.paused;
                ui.paused.show();
            }
        }else if(state == State.paused){
            if(Core.input.keyTap(KeyCode.escape)){
                if(Core.scene.getDialog() != null){
                    Core.scene.getDialog().hide();
                }
                ui.paused.hide();
                state = State.playing;
            }
        }

        if(playing() && !gameover){
            if(fire.heat <= 0f){
                gameover("Your fire went out!\n\n[lightgray]Make sure to keep it supplied with wood.");
            }

            if(player.heat <= 0f){
                gameover("You've frozen\n\n[lightgray]Make sure you return to your fire to regain heat.");
            }
        }

        if(debug){
            if(Core.input.keyTap(KeyCode.escape)){
                Core.app.exit();
            }
        }
    }

    void gameover(String reason){
        gameover = true;
        state = State.paused;
        DefaultDialog dialog = new DefaultDialog("[scarlet]Game Over");
        dialog.cont.add(reason).width(400f).wrap();
        dialog.buttons.defaults().size(150f, 40f);
        dialog.buttons.button("Menu", () -> {
            reset();
            state = State.menu;
            dialog.hide();
        });
        dialog.buttons.button("Restart", () -> {
            dialog.hide();
            play();
        });
        dialog.show();
    }

    void spawnEnemies(){
        if(times.get(0, spawnGap) && Enemy.count < maxEnemies && Mathf.chance(spawnChance * (2f - lightness))){
            int maxAdd = 10;
            int pad = -1;
            int width = (int)(Core.camera.width / tsize / 2) + pad;
            int height = (int)(Core.camera.height / tsize / 2) + pad;
            int baseX = world.t(Core.camera.position.x), baseY = world.t(Core.camera.position.y);

            //try several attempts
            for(int i = 0; i < 10; i++){
                int signX = Mathf.randomSign(), signY = Mathf.randomSign();
                boolean isY = Mathf.randomBoolean();
                int x = isY ? Mathf.range(width + maxAdd) + baseX : Mathf.random(width, width + maxAdd) * signX + baseX;
                int y = !isY ? Mathf.range(height + maxAdd) + baseY : Mathf.random(height, height + maxAdd) * signY + baseY;

                if(!world.tile(x, y).solid() && world.tile(x, y).exists() && !Mathf.within(x * tsize, y * tsize, fire.x, fire.y, 200f)){
                    Prov<Enemy> prov = enemies.random();
                    Enemy e = prov.get();
                    e.set(x * tsize, y * tsize);
                    e.add();

                    break;
                }
            }
        }
    }

    void checkItem(){
        int tx = world.t(Core.input.mouseWorldX()), ty = world.t(Core.input.mouseWorldY());
        //make items live
        Tile tile = world.tile(tx, ty);
        if(tile.item != null){
            ItemEntity entity = new ItemEntity(tile.item);
            entity.set(tx * tsize, ty * tsize);
            entity.add();
            tile.item = null;
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
