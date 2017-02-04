package com.mygdx.poop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class PoopTest extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer meterRender;
	private BitmapFont meterText;
	private Texture poop;
	private Array<Poop> poopsArray;
	private int happyMeter;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		meterRender = new ShapeRenderer();
		meterText = new BitmapFont();
		poop = new Texture(Gdx.files.internal("poop.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		poopsArray = new Array<Poop>();

		happyMeter = 100;
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

		meterRender.begin(ShapeRenderer.ShapeType.Filled);
		meterRender.setColor(1,0.5f,0,1);
		meterRender.rect(10, 480 - 10, happyMeter, 5);
		meterRender.end();

		batch.begin();
		meterText.setColor(Color.WHITE);
		meterText.draw(batch, "happy = " + happyMeter + "/100", 20 + happyMeter, 480 - 10);
		batch.end();

		//manually spawn poop
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			spawnPoop();
		}
		//reset score
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			happyMeter = 100;
		}

		Iterator<Poop> iter = poopsArray.iterator();
		while(iter.hasNext()) {
			Vector3 pos = new Vector3();
			pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(pos);
			Poop turd = iter.next();
			if(TimeUtils.nanoTime() - turd.getPoopTimer() > 5000000000L) { //5 seconds since last "poop" update
				turd.setPoopTimer(TimeUtils.nanoTime());
				if (happyMeter > 0)
					happyMeter -= 1;
			}
			if(turd.contains(pos.x, pos.y)) {
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		meterText.dispose();
		meterRender.dispose();
		poop.dispose();
	}

	private void spawnPoop() {
		Poop poop = new Poop(TimeUtils.nanoTime());
		poop.x = MathUtils.random(0, 640-64);
		poop.y = MathUtils.random(0, 400-64);
		poop.width = 64;
		poop.height = 64;
		poopsArray.add(poop);
		if (happyMeter > 0)
			happyMeter -= 1;
	}
}
