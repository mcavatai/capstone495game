package com.mygdx.poop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.poop.PoopTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Poop Demo";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new PoopTest(), config);
	}
}
