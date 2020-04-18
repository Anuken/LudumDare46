package ld.ui;

import arc.input.*;
import arc.scene.ui.*;

public class DefaultDialog extends Dialog{

    public DefaultDialog(String title){
        super(title);

        addCloseButton();
    }

    @Override
    public void addCloseButton(){
        addCloseButton(this);
    }

    public static void addCloseButton(Dialog dialog){
        dialog.keyDown(KeyCode.escape, dialog::hide);

        dialog.titleTable.add().growX();
        dialog.titleTable.button("x", dialog::hide).size(40f).right();
    }
}
