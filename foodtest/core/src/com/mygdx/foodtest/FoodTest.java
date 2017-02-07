package com.mygdx.foodtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class FoodTest extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer meterRender;
	private BitmapFont meterText;

	private Texture foodSprite;
	private Texture rockSprite;
	private Texture crossSprite;

	private Rectangle rock;
	private Rectangle food;
	private Rectangle closeButton;
	private Rectangle menuBox;

	private int happyMeter;
	private int hungryMeter;
	private long hungerTimer;
	private int menuHoverMeter;
	private int foodHoverMeter;
	private int closeHoverMeter;
	private boolean holdFoodFlag;
	private boolean foodMenuFlag;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		meterRender = new ShapeRenderer();
		meterText = new BitmapFont();
		foodSprite = new Texture(Gdx.files.internal("bread.png"));
		rockSprite = new Texture(Gdx.files.internal("rock.png"));
		crossSprite = new Texture(Gdx.files.internal("cross.png"));

		rock = new Rectangle();
		rock.x = 640/2 - 64;
		rock.y = 480/2 - 64;
		rock.width = 64;
		rock.height = 64;

		food = new Rectangle();
		food.x = 66;
		food.y = 0;
		food.width = 64;
		food.height = 64;

		closeButton = new Rectangle();
		closeButton.x = 132;
		closeButton.y = 0;
		closeButton.width = 64;
		closeButton.height = 64;

		menuBox = new Rectangle();
		menuBox.x = 0;
		menuBox.y = 0;
		menuBox.width = 64;
		menuBox.height = 64;

		happyMeter = 100;
		hungryMeter = 100;
		foodMenuFlag = false;
		holdFoodFlag = false;

		hungerTimer = 0;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		Gdx.input.setCursorPosition(640/2, 480/2);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.5f, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		Vector3 pos = new Vector3();
		pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(pos);

		meterRender.begin(ShapeRenderer.ShapeType.Filled);
		meterRender.setColor(Color.WHITE);
		meterRender.rect(0,0,64,64);
		meterRender.setColor(Color.ORANGE);
		meterRender.rect(0,480-10,hungryMeter,10);
		meterRender.rect(0,480-25,happyMeter,10);
		meterRender.rect(0,64,menuHoverMeter,10);
		meterRender.setColor(Color.YELLOW);
		meterRender.rect(66,64,foodHoverMeter,10);
		meterRender.setColor(Color.GREEN);
		meterRender.rect(132,64,closeHoverMeter,10);
		meterRender.end();

		batch.begin();
		batch.draw(rockSprite, rock.x, rock.y);
		meterText.setColor(Color.WHITE);
		meterText.draw(batch, "stomach = " + hungryMeter, hungryMeter + 20, 480 - 10);
		meterText.draw(batch, "happiness = " + happyMeter, happyMeter + 20, 480 - 25);
		meterText.setColor(Color.BLACK);
		meterText.draw(batch, "FOOD", 0, 20);
		if (foodMenuFlag) {
			batch.draw(crossSprite, closeButton.x, closeButton.y, 64, 64);
			batch.draw(foodSprite, food.x, food.y, 64, 64);
		}
		if (holdFoodFlag) {
			batch.draw(foodSprite, pos.x - 32, pos.y - 32, 64, 64);
		}
		batch.end();

		if(menuBox.contains(pos.x, pos.y) && !foodMenuFlag && !holdFoodFlag) {
			if (menuHoverMeter < 65)
				menuHoverMeter += 1;
			else {
				foodMenuFlag = true;
				menuHoverMeter = 0;
			}
		} else {
			if (!foodMenuFlag)
				menuHoverMeter = 0;
		}
		if(food.contains(pos.x, pos.y) && foodMenuFlag) {
			if (foodHoverMeter < 65)
				foodHoverMeter += 1;
			else {
				foodMenuFlag = false;
				holdFoodFlag = true;
				foodHoverMeter = 0;
			}
		} else {
			if (foodMenuFlag)
				foodHoverMeter = 0;
		}
		if(closeButton.contains(pos.x, pos.y) && foodMenuFlag) {
			if (closeHoverMeter < 65)
				closeHoverMeter += 1;
			else {
				foodMenuFlag = false;
				closeHoverMeter = 0;
			}
		} else {
			if (foodMenuFlag)
				closeHoverMeter = 0;
		}
		if(rock.contains(pos.x, pos.y) && holdFoodFlag) {
			holdFoodFlag = false;
			hungryMeter += 40;
			happyMeter += 40;
			if (hungryMeter > 100) {
				hungryMeter = 100;
			}
			if (happyMeter > 100) {
				happyMeter = 100;
			}
		}

		if (TimeUtils.nanoTime() - hungerTimer > 200000000L) {
			if (hungryMeter > 0)
				hungryMeter -= 1;
			if (hungryMeter < 40 && happyMeter > 0) {
				happyMeter -= 2;
			}
			if (happyMeter < 0)
				happyMeter = 0;
			hungerTimer = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		foodSprite.dispose();
		crossSprite.dispose();
		rockSprite.dispose();
		meterRender.dispose();
		meterText.dispose();

	}
}
