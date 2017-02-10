package com.mygdx.toystest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;

/**
 * Created by Mike on 2/8/2017.
 */
public class TestScreen implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer meterRender;
    private BitmapFont meterText;

    private Rectangle menuBox;

    private World world;
    private Rock rock;
    private RockToy toy1;
    private RockToy toy2;
    private RockToy toy3;
    private ToysTest game;

    private boolean menuOpen;

    private long timeInterval;
    private int menuMeter;
    private int toyHolding;

    public TestScreen(ToysTest game) {
        batch = new SpriteBatch();
        meterRender = new ShapeRenderer();
        meterText = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        world = new World(new Vector2(0,0), true);

        toy1 = new RockToy(world, 1, "tennisball.png", 66, 0);
        toy2 = new RockToy(world, 2, "basketball.png", 132, 0);
        toy3 = new RockToy(world, 3, "soccerball.png", 198, 0);
        rock = new Rock(world, 1);

        menuBox = new Rectangle(0,0,64,64);

        timeInterval = 0;
        menuOpen = false;
        toyHolding = 0;
        menuMeter = 0;

        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        Vector3 pos = new Vector3();
        pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(pos);

        meterRender.begin(ShapeRenderer.ShapeType.Filled);
        meterRender.setColor(Color.WHITE);
        meterRender.rect(0,0,64,64);
        meterRender.rect(10,480-20,rock.happy,10);
        meterRender.rect(10,480-40,rock.energy,10);
        meterRender.end();

        batch.begin();
        meterText.draw(batch, "happy="+rock.happy,rock.happy+10,480-20);
        meterText.draw(batch, "energy="+rock.energy,rock.energy+10,480-40);
        rock.render(batch);
        //toys will only display when menu is open

        if(menuOpen) {
            toy1.render(batch);
            toy2.render(batch);
            toy3.render(batch);
        }

        if (toyHolding == 1) {
            batch.draw(toy1.getSprite().getTexture(), pos.x - 32, pos.y - 32, 64, 64);
            menuOpen = false;
        } else if (toyHolding == 2) {
            batch.draw(toy2.getSprite().getTexture(), pos.x - 32, pos.y - 32, 64, 64);
            menuOpen = false;
        } else if (toyHolding == 3) {
            batch.draw(toy3.getSprite().getTexture(), pos.x - 32, pos.y - 32, 64, 64);
            menuOpen = false;
        }

        batch.end();

        if (menuBox.contains(pos.x,pos.y) && !menuOpen && toyHolding == 0) {
            if (menuMeter < 65)
                menuMeter++;
            else {
                menuOpen = true;
                menuMeter = 0;
            }
        } else {
            if (!menuOpen)
                menuMeter = 0;
        }

        if(toy1.coll.contains(pos.x, pos.y) && menuOpen) {
            if (toy1.hovermeter < 65)
                toy1.hovermeter++;
            else {
                menuOpen = false;
                toyHolding = 1;
                toy1.hovermeter = 0;
            }
        } else {
            if (menuOpen)
                toy1.hovermeter = 0;
        }

        if(toy2.coll.contains(pos.x, pos.y) && menuOpen) {
            if (toy2.hovermeter < 65)
                toy2.hovermeter++;
            else {
                menuOpen = false;
                toyHolding = 2;
                toy2.hovermeter = 0;
            }
        } else {
            if (menuOpen)
                toy2.hovermeter = 0;
        }

        if(toy3.coll.contains(pos.x, pos.y) && menuOpen) {
            if (toy3.hovermeter < 65)
                toy3.hovermeter++;
            else {
                menuOpen = false;
                toyHolding = 3;
                toy3.hovermeter = 0;
            }
        } else {
            if (menuOpen)
                toy3.hovermeter = 0;
        }

        if(rock.coll.contains(pos.x, pos.y) && toyHolding > 0 && rock.energy > 50) {
            if (toyHolding == rock.toyID) {
                rock.happy += 10;
            }
            toyHolding = 0;
            rock.happy += 10;
            rock.energy -= 30;
            if (rock.happy > 100) {
                rock.happy = 100;
            }
            if (rock.energy < 0) {
                rock.energy = 0;
            }
        } else if (rock.coll.contains(pos.x, pos.y) && toyHolding > 0) {
            toyHolding = 0;
            rock.happy -= 10;
            rock.energy -= 30;
            if (rock.happy < 0) {
                rock.happy = 0;
            }
            if (rock.energy < 0) {
                rock.energy = 0;
            }
        }

        if (TimeUtils.nanoTime() - timeInterval > 500000000L && rock.energy < 100) {
            rock.energy++;
            timeInterval = TimeUtils.nanoTime();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        meterRender.dispose();
        meterText.dispose();
        world.dispose();
        game.dispose();
    }
}
