package com.mygdx.sicktest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Note: As of 2/12/2017, this is the most finalized version
 * of the Rock object class.
 */
public class Rock {
    private Sprite sprite;
    private Body body;

    private Rectangle coll;

    private boolean sick;
    private int happy;
    private int energy;
    private int stomach;
    private int toyID;

    /**
     * Constructs a new Rock object
     * @param world Used to define the rock's "body"
     * @param toyID The number ID used to determine what character is generated
     */
    public Rock(World world, int toyID) {
        BodyDef rockDef = new BodyDef();
        rockDef.type = BodyDef.BodyType.StaticBody;
        rockDef.position.set(640/2 - 32, 480/2 - 32);
        body = world.createBody(rockDef);
        coll = new Rectangle();
        coll.set(rockDef.position.x, rockDef.position.y, 64, 64);
        //NOTE: toyID will determine what rock type is generated.
        // This means a specific sprite will be loaded from the asset library
        // once more sprites are added.
        sprite = new Sprite(new Texture(Gdx.files.internal("rock.png")));
        sprite.setSize(64,64);
        body.setUserData(sprite);

        this.toyID = toyID;

        sick = false;
        happy = 50;
        //energy and stomach will not be influenced in this demo.
        energy = 100;
        stomach = 100;
    }

    /**
     * Renders the rock on-screen
     * @param batch The sprite batch used for rendering
     */
    public void render(SpriteBatch batch) {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
    }

    /**
     * Sets the sickness flag on the rock.
     * @param sick The truth value of the sickness flag.
     */
    public void setSick(boolean sick) {
        this.sick = sick;
    }

    /**
     * Checks whether the rock is sick
     * @return The sickness flag value
     */
    public boolean isSick() {
        return sick;
    }

    /**
     * Updates the happiness meter of the rock.
     * @param points The amount of points (+ or -) to adjust the value by
     */
    public void setHappy(int points) {
        this.happy += points;
        if (happy < 0)
            happy = 0;
        if (happy > 100)
            happy = 100;
    }

    /**
     * Checks the happiness value of the rock.
     * @return The happiness value.
     */
    public int getHappy() {
        return happy;
    }

    /**
     * Gets the collision data for the rock
     * @return The rock collision box
     */
    public Rectangle getCollision() {
        return coll;
    }

    //NOTE: For this demo, setters and getters for other meters are omitted.
}
