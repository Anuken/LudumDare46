package ld.entity;

import arc.math.*;
import arc.util.*;

public abstract class TimedEntity extends Entity implements Scaled{
    public float lifetime, time;

    @Override
    public void update(){
        time += Time.delta();
        if(time >= lifetime){
            remove();
        }
    }

    @Override
    public float fin(){
        return Mathf.clamp(time / lifetime);
    }
}
