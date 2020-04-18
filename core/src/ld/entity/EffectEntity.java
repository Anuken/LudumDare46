package ld.entity;

import arc.graphics.*;

public class EffectEntity extends TimedEntity{
    public Effect effect;
    public float rotation;
    public Color color;
    public Object data;

    public EffectEntity(Effect effect, float x, float y, float rotation, Color color, Object data){
        this.effect = effect;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.color = color;
        this.data = data;
        this.lifetime = effect.lifetime;
    }

    @Override
    public void draw(){
        effect.render(id, color, time, rotation, x, y, data);
    }
}
