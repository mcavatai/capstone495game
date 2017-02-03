package com.mygdx.poop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class PoopTest extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Texture poop;
	private Array<Rectangle> poopsArray;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		poop = new Texture(Gdx.files.internal("poop.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		poopsArray = new Array<Rectangle>();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Rectangle turd: poopsArray) {
			batch.draw(poop, turd.x, turd.y, turd.width, turd.height);
		}
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			spawnPoop();
		}

		Iterator<Rectangle> iter = poopsArray.iterator();
		while(iter.hasNext()) {
			Vector3 pos = new Vector3();
			pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(pos);
			Rectangle turd = iter.next();
			if(turd.contains(pos.x, pos.y)) {
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		poop.dispose();
	}

	private void spawnPoop() {
		Rectangle poop = new Rectangle();
		poop.x = MathUtils.random(0, 640-64);
		poop.y = MathUtils.random(0, 400-64);
		poop.width = 64;
		poop.height = 64;
		poopsArray.add(poop);
	}
}
