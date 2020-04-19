package ld.entity;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;

import static ld.Game.*;

public class Collisions{
    //range for tile collision scanning
    private static final int r = 1;
    //move in 1-unit chunks
    private static final float seg = 1f;

    //tile collisions
    private static Rect tmp = new Rect();
    private static Vec2 vector = new Vec2();
    private static Vec2 l1 = new Vec2();
    private static Rect r1 = new Rect();
    private static Rect r2 = new Rect();

    //entity collisions
    private static Array<Entity> arrOut = new Array<>();

    public static void move(Entity entity, float deltax, float deltay){

        boolean movedx = false;

        while(Math.abs(deltax) > 0 || !movedx){
            movedx = true;
            moveDelta(entity, Math.min(Math.abs(deltax), seg) * Mathf.sign(deltax), 0, true);

            if(Math.abs(deltax) >= seg){
                deltax -= seg * Mathf.sign(deltax);
            }else{
                deltax = 0f;
            }
        }

        boolean movedy = false;

        while(Math.abs(deltay) > 0 || !movedy){
            movedy = true;
            moveDelta(entity, 0, Math.min(Math.abs(deltay), seg) * Mathf.sign(deltay), false);

            if(Math.abs(deltay) >= seg){
                deltay -= seg * Mathf.sign(deltay);
            }else{
                deltay = 0f;
            }
        }
    }

    public static void moveDelta(Entity entity, float deltax, float deltay, boolean x){

        Rect rect = r1;
        entity.tileHit(rect);
        entity.tileHit(r2);
        rect.x += deltax;
        rect.y += deltay;

        int tilex = Math.round((rect.x + rect.width / 2) / tsize), tiley = Math.round((rect.y + rect.height / 2) / tsize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx + tilex, wy = dy + tiley;
                if(solid(wx, wy)){
                    tmp.setSize(tsize).setCenter(wx * tsize, wy * tsize);

                    if(tmp.overlaps(rect)){
                        Vec2 v = Geometry.overlap(rect, tmp, x);
                        rect.x += v.x;
                        rect.y += v.y;
                    }
                }
            }
        }

        entity.x = (entity.x + rect.x - r2.x);
        entity.y = (entity.y + rect.y - r2.y);
    }

    private static boolean solid(int x, int y){
        return world.tile(x, y).solid();
    }
}
