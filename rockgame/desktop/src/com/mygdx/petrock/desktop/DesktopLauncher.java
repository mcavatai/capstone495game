package com.mygdx.petrock.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.petrock.PetRockGame;

/**
 * The desktop launcher for the game.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "DEMO BUILD 1";
		config.x = 0;
		config.y = 0;
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new PetRockGame(), config);
	}
}
