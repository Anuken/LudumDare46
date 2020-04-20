package ld.teavm;

import arc.backend.teavm.*;
import arc.backend.teavm.TeaApplication.*;
import ld.*;
import org.teavm.jso.browser.*;
import org.teavm.jso.dom.html.*;

public class TeaVMLauncher{

    public static void main(String[] args){
        TeaApplicationConfig config = new TeaApplicationConfig();
        config.canvas = (HTMLCanvasElement)Window.current().getDocument().getElementById("main-canvas");

        /*
        Window.current().addEventListener("resize", new EventListener<Event>(){
            @Override
            public void handleEvent(Event evt){
                scaleCanvas();
            }
        });*/

        new TeaApplication(new Game(), config).start();

        //scaleCanvas();
    }

    //this maintains a constant aspect ratio - unused and broken
    static void scaleCanvas(){
        final int WIDTH = 800;
        final int HEIGHT = 600;

        HTMLCanvasElement element = (HTMLCanvasElement)Window.current().getDocument().getElementById("main-canvas");
        int innerWidth = Window.current().getInnerWidth();
        int innerHeight = Window.current().getInnerHeight();
        int newWidth = innerWidth;
        int newHeight = innerHeight;
        float ratio = innerWidth / (float)innerHeight;
        float viewRatio = WIDTH / (float)HEIGHT;

        if(ratio > viewRatio){
            newWidth = (int)(innerHeight * viewRatio);
        }else{
            newHeight = (int)(innerWidth / viewRatio);
        }

        element.setAttribute("width", "" + newWidth + "px");
        element.setAttribute("height", "" + newHeight + "px");
        element.setAttribute("style",
        "width: " + newWidth + "px; " +
        "height: " + newHeight + "px; " +
        "top: " + (int)((innerHeight - newHeight) * 0.5f) + "px; " +
        "left: " + (int)((innerWidth - newWidth) * 0.5f) + "px; " +
        "position: absolute;");
    }
}
