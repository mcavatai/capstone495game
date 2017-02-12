package com.mygdx.sicktest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.sicktest.SickTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sickness Demo";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new SickTest(), config);
	}
}
