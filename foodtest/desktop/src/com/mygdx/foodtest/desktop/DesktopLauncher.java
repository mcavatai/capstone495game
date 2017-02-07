package com.mygdx.foodtest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.foodtest.FoodTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Food Demo";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new FoodTest(), config);
	}
}
