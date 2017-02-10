package com.mygdx.toystest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ToysTest extends Game {
	
	@Override
	public void create () {
		//Make some screens here
		TestScreen screen = new TestScreen(this);
		setScreen(screen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		screen.dispose();
	}

	public void changeScreen(Screen screen) {
		this.setScreen(screen);
	}
}
