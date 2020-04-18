package ld;

import arc.KeyBinds.*;
import arc.input.InputDevice.*;
import arc.input.*;

public enum Bind implements KeyBind{
    move_x(new Axis(KeyCode.a, KeyCode.d)),
    move_y(new Axis(KeyCode.s, KeyCode.w)),
    pickup(KeyCode.mouseLeft);

    private final KeybindValue keyboard, controller;

    Bind(KeybindValue keyboard, KeybindValue controller){
        this.keyboard = keyboard;
        this.controller = controller;
    }

    Bind(KeybindValue defaultValue){
        this(defaultValue, defaultValue);
    }

    @Override
    public KeybindValue defaultValue(DeviceType type){
        return type == DeviceType.keyboard ? keyboard : controller;
    }
}
