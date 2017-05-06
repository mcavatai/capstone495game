package com.mygdx.petrock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The pet rock Game entity
 */
public class PetRockGame extends Game {
	
	@Override
	public void create () {
		GameScreen gscreen = new GameScreen(this);
		setScreen(gscreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
