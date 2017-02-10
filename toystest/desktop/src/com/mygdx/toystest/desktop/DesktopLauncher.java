package com.mygdx.toystest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.toystest.ToysTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Toys Demo";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new ToysTest(), config);
	}
}
