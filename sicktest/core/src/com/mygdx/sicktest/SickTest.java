package com.mygdx.sicktest;

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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;

public class SickTest extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer meterRender;
	private BitmapFont meterText;

	private World world;
	private Rock rock;

	private Rectangle menuBox;
	private Rectangle pill;

	// NOTE: In final game, each button will be its own object, with its own
	// internal timer and state variables.
	private int menuHoverTime;
	private int medsHoverTime;
	private boolean menuIsOpen;
	private boolean pillIsHeld;

	private long timeInterval = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		meterRender = new ShapeRenderer();
		meterText = new BitmapFont();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		world = new World(new Vector2(0,0), true);

		rock = new Rock(world, 1);
		menuBox = new Rectangle(0,0,64,64);
		pill = new Rectangle(66,0,64,64);

		menuHoverTime = 0;
		medsHoverTime = 0;

		menuIsOpen = false;
		pillIsHeld = false;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		Vector3 pos = new Vector3();
		pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(pos);

		meterRender.begin(ShapeRenderer.ShapeType.Filled);
		meterRender.setColor(Color.WHITE);
		meterRender.rect(0,0,64,64);
		meterRender.rect(10,480-20,rock.getHappy(),10);
		meterRender.end();

		batch.begin();
		meterText.draw(batch, "happy="+rock.getHappy()+"::isSick="+rock.isSick(),rock.getHappy()+10,480-20);
		rock.render(batch);

		if (menuIsOpen) {
			batch.draw(new Texture(Gdx.files.internal("pill.png")), 66, 0, 64, 64);
		}

		if (pillIsHeld) {
			batch.draw(new Texture(Gdx.files.internal("pill.png")), pos.x - 32, pos.y - 32, 64, 64);
		}

		batch.end();

		if (menuBox.contains(pos.x, pos.y) && !menuIsOpen && !pillIsHeld) {
			if (menuHoverTime < 65)
				menuHoverTime++;
			else {
				menuIsOpen = true;
				menuHoverTime = 0;
			}
		} else  {
			if (!menuIsOpen)
				menuHoverTime = 0;
		}

		if (pill.contains(pos.x, pos.y) && menuIsOpen) {
			if (medsHoverTime < 65)
				medsHoverTime++;
			else {
				menuIsOpen = false;
				pillIsHeld = true;
				medsHoverTime = 0;
			}
		} else {
			if (menuIsOpen)
				medsHoverTime = 0;
		}

		if (rock.getCollision().contains(pos.x, pos.y) && pillIsHeld) {
			if (rock.isSick()) {
				rock.setSick(false);
				rock.setHappy(15);
			} else {
				rock.setHappy(-15);
			}
			pillIsHeld = false;
		}

		//toggle sickness manually
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			rock.setSick(!rock.isSick());
		}

		if (TimeUtils.nanoTime() - timeInterval > 2000000000L && rock.isSick()) {
			rock.setHappy(-3);
			timeInterval = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		meterRender.dispose();
		meterText.dispose();
		world.dispose();
	}
}
