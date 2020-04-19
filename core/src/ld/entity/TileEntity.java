package ld.entity;

import arc.math.geom.*;
import ld.World.*;

import static ld.Game.*;

public class TileEntity extends Entity{
    public final Tile tile;

    public TileEntity(Point2 tile){
        this.tile = world.tile(tile.x, tile.y);
        set(tile.x * tsize, tile.y * tsize);
        add();
    }
}
