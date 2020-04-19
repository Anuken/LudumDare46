package ld.entity;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ld.*;
import ld.gfx.*;

public class TorchEntity extends TileEntity{
    Interval time = new Interval();

    public TorchEntity(Point2 tile){
        super(tile);
    }

    @Override
    public void update(){
        super.update();

        if(tile.wall != Block.torchBlock){
            remove();
        }

        if(Mathf.chance(0.05f * Time.delta())){
            Fx.fire.at(x, y + 6, 0.5f);
        }

        if(time.get(0, 30)){
            Game.control.nearby(x, y, 30f).each(e -> e instanceof Enemy, n -> {
                Enemy e = (Enemy)n;
                e.damage(9f * Time.delta());

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

        Drawf.light(x, y, (120f + Mathf.absin(6f, 10f)), Color.orange, 0.7f);
    }

    @Override
    public float clipSize(){
        return 400;
    }
}
