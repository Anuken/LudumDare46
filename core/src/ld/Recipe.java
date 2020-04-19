package ld;

import arc.struct.*;
import arc.util.ArcAnnotate.*;

public class Recipe{
    private static ObjectMap<Item, ObjectMap<Item, Recipe>> recipeMap = new ObjectMap<>();
    public final static Array<Recipe> recipes = Array.with(
    new Recipe(Item.stick, Item.rock, Item.axe),
    new Recipe(Item.log, Item.rock, Item.axe),
    new Recipe(Item.axe, Item.rock, Item.pickaxe),

    new Recipe(Item.stick, Item.glowingRock, Item.fireAxe),
    new Recipe(Item.log, Item.glowingRock, Item.fireAxe),
    new Recipe(Item.axe, Item.glowingRock, Item.firePickaxe),

    new Recipe(Item.frozenStick, Item.glowingRock, Item.torch),
    new Recipe(Item.stick, Item.glowingRock, Item.torch),
    new Recipe(Item.log, Item.glowingRock, Item.torch),

    new Recipe(Item.frozenStick, Item.coal, Item.torch),
    new Recipe(Item.stick, Item.coal, Item.torch),
    new Recipe(Item.log, Item.coal, Item.torch),

    new Recipe(Item.stick, Item.gem, Item.gemTool),
    new Recipe(Item.log, Item.gem, Item.gemTool),
    new Recipe(Item.fireAxe, Item.gem, Item.gemTool),
    new Recipe(Item.firePickaxe, Item.gem, Item.gemTool),

    new Recipe(Item.torch, Item.gem, Item.gemTorch)
    );

    static{
        for(Recipe r : recipes){
            recipeMap.getOr(r.lower(), ObjectMap::new).put(r.upper(), r);
        }
    }

    public final Item first, second, result;

    public Recipe(Item first, Item second, Item result){
        this.first = first;
        this.second = second;
        this.result = result;
    }

    Item lower(){
        return first.ordinal() < second.ordinal() ? first : second;
    }

    Item upper(){
        return first.ordinal() >= second.ordinal() ? first : second;
    }

    public static @Nullable Recipe get(Item first, Item second){
        Item lower = first.ordinal() < second.ordinal() ? first : second;
        Item upper = first.ordinal() < second.ordinal() ? second : first;

        return recipeMap.get(lower) == null ? null : recipeMap.get(lower).get(upper);
    }

    public static boolean has(Item first, Item second){
        return get(first, second) != null;
    }
}
