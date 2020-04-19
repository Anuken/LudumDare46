package ld.entity;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.*;
import ld.gfx.*;

public class GemTorchEntity extends TileEntity{
    Interval time = new Interval();

    public GemTorchEntity(Point2 tile){
        super(tile);
    }

    @Override
    public void update(){
        super.update();

        if(tile.wall != Block.gemTorchBlock){
            remove();
        }

        if(Mathf.chance(0.06f * Time.delta())){
            Fx.fire.at(x, y + 6, 0.5f);
        }

        if(time.get(0, 25)){
            Game.control.nearby(x, y, 40f).each(e -> e instanceof Enemy, n -> {
                Enemy e = (Enemy)n;
                e.damage(12f * Time.delta());

                //torches die when someone gets close
                if(within(e, 6f)){
                    Fx.fireballs.at(x, y);
                    Fx.pickup.at(x, y);
                    tile.wall = Block.none;
                }
            });
        }
    }

    @Override
    public void draw(){
        super.draw();

        Drawf.light(x, y, (160f + Mathf.absin(6f, 10f)), Tmp.c1.set(Color.orange).lerp(Color.purple, 0.5f), 0.8f);
    }

    @Override
    public float clipSize(){
        return 400;
    }
}
