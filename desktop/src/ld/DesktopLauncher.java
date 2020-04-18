package ld;

import arc.backend.sdl.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new SdlApplication(new Game(), new SdlConfig(){{
		    title = "LD46";
		    width = 880;
		    height = 600;
		    maximized = true;
        }});
	}
}
