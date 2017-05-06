package com.mygdx.petrock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.Iterator;
import java.util.Random;

/**
 * The screen where gameplay occurs.
 */
public class GameScreen implements Screen {

    private PetRockGame game;
    private World world;

    private Rock rock;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer meterRender;
    private BitmapFont meterText;

    private boolean foodOpen, toysOpen, medsOpen;

    private int toyHeld;
    private int foodHeld;
    private boolean pillHeld;

    //used for updating hunger bar
    private long hungerTimer;
    //used for updating energy bar
    private long energyTimer;

    private HoverButton[] menuButtons;
    private Favable[] foods;
    private Favable[] toys;
    private HoverButton[] meds;
    private HoverButton trashcan;

    private Array<Poop> poopArray;

    /**
     * Constructs the game screen.
     *
     * @param game Reference to the game entity.
     */
    public GameScreen(PetRockGame game) {
        batch = new SpriteBatch();
        meterRender = new ShapeRenderer();
        meterText = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        world = new World(new Vector2(0, 0), true);
        //TODO implement random number gen to select from rock types 1-3
        rock = new Rock(world, 1);

        foodOpen = false;
        medsOpen = false;
        toysOpen = false;

        hungerTimer = 0;
        energyTimer = 0;

        toyHeld = 0;
        foodHeld = 0;
        pillHeld = false;

        menuButtons = new HoverButton[3];
        foods = new Favable[3];
        toys = new Favable[3];
        meds = new HoverButton[1];

        poopArray = new Array<Poop>();

        createInterfaceElements();

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
        meterRender.rect(0, 0, 640, 66);
        meterRender.end();

        meterRender.begin(ShapeRenderer.ShapeType.Filled);
        batch.begin();
        //render rock and meters
        rock.render(batch, meterRender, meterText);
        //render menu bar
        for (HoverButton button : menuButtons) {
            button.render(batch, meterRender);
        }
        trashcan.render(batch, meterRender);
        //render open menu if applicable
        if (foodOpen) {
            for (Favable food : foods) {
                food.render(batch, meterRender);
            }
        }
        if (medsOpen) {
            for (HoverButton med : meds) {
                med.render(batch, meterRender);
            }
        }
        if (toysOpen) {
            for (Favable toy : toys) {
                toy.render(batch, meterRender);
            }
        }
        //render held item if applicable
        if (foodHeld > 0) {
            batch.draw(foods[foodHeld - 1].getSprite().getTexture(), pos.x - 32, pos.y - 32, 64 ,64);
        }
        if (toyHeld > 0) {
            batch.draw(toys[toyHeld - 1].getSprite().getTexture(), pos.x - 32, pos.y - 32, 64 ,64);
        }
        if (pillHeld) {
            //TODO modify later to account for multiple items if needed
            batch.draw(meds[0].getSprite().getTexture(), pos.x - 32, pos.y - 32, 64, 64);
        }
        //render poop if applicable
        for (Poop turd : poopArray) {
            turd.render(batch, meterRender);
        }
        //8 preset positions. refer to poopdemo when dealing with rendering this

        batch.end();
        meterRender.end();


        /* AFTER RENDERING -- LOGIC */

        //MENU LOGIC
        /*
        * Open menu only when no item is held.
        * Close any open menus when a new menu is open.
        * Close menu when an item is picked up or the close button is used.
        */

        //trash can
        if (!isNothingHeld() && trashcan.getColl().contains(pos.x, pos.y)) {
            foodHeld = 0;
            toyHeld = 0;
            pillHeld = false;
        }

        //food menu
        if (!foodOpen && menuButtons[0].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[0].getHovermeter() < 65)
                menuButtons[0].incrementHovermeter();
            else {
                foodOpen = true;
                medsOpen = false;
                toysOpen = false;
                menuButtons[0].setHovermeter(0);
            }
        } else if (foodOpen && menuButtons[0].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[0].getHovermeter() < 65)
                menuButtons[0].incrementHovermeter();
            else {
                foodOpen = false;
                medsOpen = false;
                toysOpen = false;
                menuButtons[0].setHovermeter(0);
            }
        } else {
            menuButtons[0].setHovermeter(0);
        }

        if (foodOpen && isNothingHeld()){
            for (Favable food : foods) {
                if (food.getColl().contains(pos.x, pos.y)) {
                    if (food.getHovermeter() < 65) {
                        food.incrementHovermeter();
                    } else {
                        foodHeld = food.getID();
                        food.setHovermeter(0);
                        foodOpen = false;
                    }
                } else
                    food.setHovermeter(0);
            }
        }

        //toy menu
        if (!toysOpen && menuButtons[1].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[1].getHovermeter() < 65)
                menuButtons[1].incrementHovermeter();
            else {
                foodOpen = false;
                medsOpen = false;
                toysOpen = true;
                menuButtons[1].setHovermeter(0);
            }
        } else if (toysOpen && menuButtons[1].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[1].getHovermeter() < 65)
                menuButtons[1].incrementHovermeter();
            else {
                foodOpen = false;
                medsOpen = false;
                toysOpen = false;
                menuButtons[1].setHovermeter(0);
            }
        } else {
            menuButtons[1].setHovermeter(0);
        }

        if (toysOpen && isNothingHeld()) {
            for (Favable toy: toys) {
                if (toy.getColl().contains(pos.x, pos.y)) {
                    if (toy.getHovermeter() < 65) {
                        toy.incrementHovermeter();
                    } else {
                        toyHeld = toy.getID();
                        toy.setHovermeter(0);
                        toysOpen = false;
                    }
                } else
                    toy.setHovermeter(0);
            }
        }

        //med menu
        if (!medsOpen && menuButtons[2].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[2].getHovermeter() < 65)
                menuButtons[2].incrementHovermeter();
            else {
                foodOpen = false;
                medsOpen = true;
                toysOpen = false;
                menuButtons[2].setHovermeter(0);
            }
        } else if (medsOpen && menuButtons[2].getColl().contains(pos.x, pos.y) && isNothingHeld()) {
            if (menuButtons[2].getHovermeter() < 65)
                menuButtons[2].incrementHovermeter();
            else {
                foodOpen = false;
                medsOpen = false;
                medsOpen = false;
                menuButtons[2].setHovermeter(0);
            }
        } else {
            menuButtons[2].setHovermeter(0);
        }

        if (medsOpen && isNothingHeld()) {
            for (HoverButton pill: meds) {
                if (pill.getColl().contains(pos.x, pos.y)) {
                    if (pill.getHovermeter() < 65) {
                        pill.incrementHovermeter();
                    } else {
                        pillHeld = true;
                        pill.setHovermeter(0);
                        medsOpen = false;
                    }
                } else
                    pill.setHovermeter(0);
            }
        }

        //ITEM LOGIC
        /*
        * Item can only be picked up when no item is being held.
        * Picking up an item should set all other categories' "item held" states to 0/false.
        * Putting an item on the rock or in the trash should set "item held" to false.
        * Food --> +stomach, +happy(favorite)
        * Toys --> +happy(favorite = x2), -energy
        * Meds --> !sick
        * Note exceptions in rock logic section.
        */

        //POOP LOGIC
        /*
        * Poop has a 1/4(?) chance of spawning a little while after you feed the rock.
        * Up to 8 can be on screen at once.
        * When poop spawns, rock's happiness decreases for each poop on screen over time.
        * If 8 poops are on screen there is a 1/8 chance that when the happiness decrease happens,
        *   the rock will get sick.
        */

        Iterator<Poop> iter = poopArray.iterator();
        while(iter.hasNext()) {
            Poop turd = iter.next();
            if (turd.getColl().contains(pos.x, pos.y)) {
                if (turd.getHovermeter() < 17) {
                    turd.incrementHovermeter();
                } else {
                    iter.remove();
                }
            } else {
                turd.setHovermeter(0);
            }
        }

        //TODO work on balancing.
        //TODO write poop timers
        //ROCK LOGIC
        /*
        * Energy drops when playing with toys. Restores steadily over time?
        * Hunger drops steadily over time, regardless of state. Food replenishes.
        * Happiness increases when playing with toys.
        * Happiness drops when:
        * ---Hunger is too low (extreme low = sickness. can't eat without meds first)
        * ---Playing while energy is too low
        * ---Poop is on-screen
        * ---Rock is sick
        */
        //rock passive stat updates
        //stomach passive
        if (TimeUtils.nanoTime() - hungerTimer > 3000000000L) {
            rock.setStomach(-1);
            if (rock.getStomach() < 40 && rock.getHappy() > 0) {
                rock.setHappy(-1);
            }
            if (rock.getStomach() < 10) {
                rock.setSickFlag(true);
            }
            if (rock.getStomach() > 80) {
                if (poopArray.size < 8) {
                    Random flipCoin = new Random();
                    int result = flipCoin.nextInt(8);
                    if (result == 1) {
                        int x = MathUtils.random(64, 640 - 64);
                        int y = MathUtils.random(76, 480 - 64);
                        while (rock.getColl().contains(x, y)) {
                            x = MathUtils.random(64, 640 - 64);
                            y = MathUtils.random(76, 480 - 64);
                        }
                        Poop turd = new Poop(world, "poop.png", x, y);
                        poopArray.add(turd);
                    }
                } else {
                    Random flipCoin = new Random();
                    int result = flipCoin.nextInt(8);
                    if (result == 1) {
                        rock.setSickFlag(true);
                    }
                }
            }
            hungerTimer = TimeUtils.nanoTime();
        }

        //energy passive
        if (TimeUtils.nanoTime() - energyTimer > 3000000000L) {
            if (!rock.isSickFlag()) {
                rock.setEnergy(3);
            } else {
                rock.setHappy(-1);
            }
            energyTimer = TimeUtils.nanoTime();
        }

        //food
        //replenish energy
        //if sick, replenish less energy, maybe decrease happiness?
        //favorite food increases happiness twice
        if (rock.getColl().contains(pos.x, pos.y) && foodHeld > 0) {
            if (!rock.isSickFlag()) {
                rock.setStomach(20);
                rock.setHappy(10);
                if (rock.getFavoriteID() == foodHeld) {
                    rock.setHappy(10);
                }
            } else {
                rock.setStomach(5);
                rock.setHappy(-10);
            }
            foodHeld = 0;
        }

        //toys
        //consumes energy
        //increases happiness if rock is not too tired (sick, or less than 40 E)
        //decreases happiness otherwise
        //double happiness if favorite
        if (rock.getColl().contains(pos.x, pos.y) && toyHeld > 0) {
            if (rock.getEnergy() > 40 && !rock.isSickFlag()) {
                if (toyHeld == rock.getFavoriteID()) {
                    rock.setHappy(10);
                }
                rock.setHappy(10);
                rock.setEnergy(-15);
            } else {
                rock.setEnergy(-20);
                rock.setHappy(-10);
            }

            toyHeld = 0;
        }

        //meds
        //if sick, pill makes rock not sick, happy
        //not sick = -happy, maybe gets sick?
        if (rock.getColl().contains(pos.x, pos.y) && pillHeld) {
            if (rock.isSickFlag()) {
                rock.setSickFlag(false);
                rock.setHappy(15);
            } else {
                Random flipCoin = new Random();
                int result = flipCoin.nextInt(2);
                rock.setHappy(-15);
                if (result == 1) {
                    rock.setSickFlag(true);
                }
            }
            pillHeld = false;
        }

        //DEBUG COMMANDS -- manually change game elements here
        //drop item
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            foodHeld = 0;
            toyHeld = 0;
            pillHeld = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (poopArray.size < 8) {
                int x = MathUtils.random(64, 640 - 64);
                int y = MathUtils.random(76, 480 - 64);
                while (rock.getColl().contains(x,y)) {
                    x = MathUtils.random(64, 640 - 64);
                    y = MathUtils.random(76, 480 - 64);
                }
                Poop turd = new Poop(world, "poop.png", x,y);
                poopArray.add(turd);
            }
        }
    }

    /**
     * Builds the menu bar/buttons
     */
    private void createInterfaceElements() {
        //menu
        menuButtons[0] = new HoverButton(world, "menuapple.png", 10, 10);
        menuButtons[1] = new HoverButton(world, "box.png", 76, 10);
        menuButtons[2] = new HoverButton(world, "menupill.png", 140, 10);

        //foods
        foods[0] = new Favable(world, "bread.png", 10, 78, 1);
        foods[1] = new Favable(world, "pizza.png", 76, 78, 2);
        foods[2] = new Favable(world, "apple.png", 142, 78, 3);

        //toys
        toys[0] = new Favable(world, "basketball.png", 76, 78, 1);
        toys[1] = new Favable(world, "tennisball.png", 142, 78, 2);
        toys[2] = new Favable(world, "soccerball.png", 208, 78, 3);

        //meds
        meds[0] = new HoverButton(world, "pill.png", 140, 78);

        //trashcan
        trashcan = new HoverButton(world, "trash.png", 630 - 64, 10);
    }

    private boolean isNothingHeld() {
        return (foodHeld == 0 && toyHeld == 0 && pillHeld == false);
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
        meterRender.dispose();
        batch.dispose();
        meterText.dispose();
    }
}
