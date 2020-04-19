package ld.entity;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import ld.*;
import ld.entity.Fx.*;
import ld.gfx.*;

import static ld.Game.*;

public class Fire extends Entity{
    static final ObjectMap<Item, Item> recipes = ObjectMap.of(
        Item.frozenKey, Item.key,
        Item.axe, Item.fireAxe,
        Item.pickaxe, Item.firePickaxe,
        Item.ore, Item.gem,
        Item.rock, Item.glowingRock
    );

    public float heat, smoothHeat;

    @Override
    public float clipSize(){
        return 300f;
    }

    @Override
    public boolean clickable(){
        return player.item != null && (player.item.flammability > 0 || recipes.containsKey(player.item));
    }

    @Override
    public void clicked(){
        Fx.pickup.at(player);
        Fx.itemMove.at(player.x, player.y + 6, 0, new ItemMove(this, player.item));

        Time.run(Fx.pickup.lifetime*0.75f, () -> {
            Fx.fireballs.at(this);
            Sounds.burnItem.play(this);
        });

        //craft
        if(recipes.containsKey(player.item)){
            Item result = recipes.get(player.item);
            Time.run(Fx.pickup.lifetime, () -> {
                ItemEntity.create(result, x, y).velocity.set(player).sub(this).limit(5f);
            });
        }else{
            heat += player.item.flammability;
            heat = Math.min(heat, 1.4f);
        }

        player.item = null;
    }

    @Override
    public void update(){
        super.update();

        if(Mathf.chance(0.02 * Time.delta() * heat)){
            Fx.spark.at(this);
        }

        if(Mathf.chance(0.2 * Time.delta() * heat)){
            Fx.fire.at(x, y, heat);
        }

        ItemEntity item = control.closest(x, y, 7f, e -> e instanceof ItemEntity && ((ItemEntity)e).item.flammability > 0);
        if(item != null){
            Fx.pickup.at(item);
            Fx.fireballs.at(this);
            heat += item.item.flammability;
            item.remove();
        }

        //heat loss
        heat -= Time.delta() / fireDuration;
        heat = Mathf.clamp(heat);
    }

    @Override
    public void draw(){
        Draw.z(y + 6);

        Draw.rect("fire-base", x, y);

        Draw.z(y - 1);
        smoothHeat = Mathf.lerpDelta(smoothHeat, heat, 0.1f);

        renderer.drawNormal(() -> {
            Tmp.tr1.set(Core.atlas.texture());

            float scale = smoothHeat;
            float width = 100 * scale, height = 100 * scale;

            Draw.shader(Shaders.fire);
            Draw.rect(Tmp.tr1, x, y + height * 0.43f, width, -height);
            Draw.shader();
        });

        Drawf.light(x, y, (220f + Mathf.absin(6f, 10f)) * smoothHeat + 50f, Color.orange, Mathf.lerp(smoothHeat, 1.1f, 0.4f));
    }

    @Override
    public boolean solid(){
        return true;
    }
}
