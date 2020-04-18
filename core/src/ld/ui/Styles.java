package ld.ui;

import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.Button.*;
import arc.scene.ui.CheckBox.*;
import arc.scene.ui.Dialog.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.KeybindDialog.*;
import arc.scene.ui.Label.*;
import arc.scene.ui.ScrollPane.*;
import arc.scene.ui.Slider.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.TextField.*;
import ld.*;

import static ld.ui.Tex.*;

public class Styles{
    public static final Drawable
    dialogDim = white.tint(0, 0, 0, 0.45f),
    blankSelect = white.tint(1, 1, 1, 0.4f);

    public static final ButtonStyle
    defaultb = new ButtonStyle(){{
        down = buttonDown;
        up = button;
    }},
    toggleb = new ButtonStyle(){{
        checked = buttonDown;
        down = buttonDown;
        up = button;
    }};
    
    public static final TextButtonStyle
    defaultt = new TextButtonStyle(){{
        over = buttonOver; 
        disabled = button; 
        font = Game.font;
        fontColor = Color.white; 
        disabledFontColor = Color.gray; 
        down = buttonDown; 
        up = button; 
    }},
    togglet = new TextButtonStyle(){{
        font = Game.font;
        fontColor = Color.white;
        checked = buttonDown;
        down = buttonDown;
        up = button;
        over = buttonOver;
        disabled = button;
        disabledFontColor = Color.gray;
    }};

    public static final ImageButtonStyle
    defaulti = new ImageButtonStyle(){{
        down = buttonDown;
        up = button;
    }},

    togglei = new ImageButtonStyle(){{
        checked = buttonDown;
        down = buttonDown;
        up = button;
    }};

    public static final ScrollPaneStyle
    defaultPane = new ScrollPaneStyle(){{
        background = blank;
        vScroll = scroll;
        vScrollKnob = scrollKnobVertical;
    }};

    public static final DialogStyle
    defaultWindow = new DialogStyle(){{
        titleFont = Game.font;
        background = window;
        titleFontColor = Color.orange;
    }},
    dialog = new DialogStyle(){{
        stageBackground = dialogDim;
        titleFont = Game.font;
        background = window;
        titleFontColor = Color.white;
    }};

    public static final SliderStyle
    defaultSlider = new SliderStyle(){{
        background = slider;
        knob = sliderKnob;
        knobOver = sliderKnobOver;
        knobDown = sliderKnobDown;
    }};

    public static final LabelStyle
    defaultLabel = new LabelStyle(){{
        font = Game.font;
        fontColor = Color.white;
    }};

    public static final TextFieldStyle
    defaultField = new TextFieldStyle(){{
        font = Game.font;
        fontColor = Color.white;
        disabledFontColor = Color.gray;
        selection = Tex.selection;
        background = button;
        cursor = Tex.cursor;
        messageFont = Game.font;
        messageFontColor = Color.gray;
    }};

    public static final CheckBoxStyle
    defaultCheck = new CheckBoxStyle(){{
        checkboxOn = checkOn;
        checkboxOff = checkOff;
        checkboxOver = checkOver;
        font = Game.font;
        fontColor = Color.white;
        disabledFontColor = Color.gray;
    }};

    public static final KeybindDialogStyle
    defaultKeybind = new KeybindDialogStyle(){{

    }};
    
}
