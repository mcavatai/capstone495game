package com.mygdx.petrock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Object representing the pet rock.
 */
public class Rock {
    private Sprite sprite;
    private Body body;

    private Rectangle coll;

    private int happy;
    private int energy;
    private int stomach;
    private boolean sickFlag;

    private int favoriteID;

    public Rock(World world, int id) {
        BodyDef rockDef = new BodyDef();
        rockDef.type = BodyDef.BodyType.StaticBody;
        rockDef.position.set(640/2-32, 480/2-32);
        body = world.createBody(rockDef);

        coll = new Rectangle(rockDef.position.x, rockDef.position.y, 64, 64);

        //TODO call different rock sprites depending on ID
        sprite = new Sprite(new Texture(Gdx.files.internal("rock.png")));
        sprite.setSize(64,64);
        body.setUserData(sprite);

        this.favoriteID = id;

        happy = 50;
        energy = 100;
        stomach = 100;
    }

    public void render(SpriteBatch batch, ShapeRenderer meterRender, BitmapFont textRender) {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
        //draw meters
        meterRender.rect(10, 480 - 10, this.happy, 10);
        textRender.draw(batch, "HAPPINESS", 10, 480-10);
        //this one draws from right to left
        meterRender.rect(640 - 10, 480 - 10, -this.energy, 10);
        textRender.draw(batch, "ENERGY", 540, 480-10);
        //this one draws from right to left
        meterRender.rect(640 - 10, 480 - 30, -this.stomach, 10);
        textRender.draw(batch, "STOMACH", 540, 480-30);
        textRender.draw(batch, "SICK = " + this.isSickFlag(), 640/2, 480-20);
    }

    /**
     * Sets the sickness flag on the rock.
     * @param sick The truth value of the sickness flag.
     */
    public void setSickFlag(boolean sick) {
        sickFlag = sick;
    }

    /**
     * Checks whether the rock is sick
     * @return The sickness flag value
     */
    public boolean isSickFlag() {
        return sickFlag;
    }

    /**
     * Updates the happiness meter of the rock.
     * @param points The amount of points (+ or -) to adjust the value by
     */
    public void setHappy(int points) {
        happy += points;
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
     * Updates the energy meter
     * @param points The amount of points (+ or -) to adjust the value by
     */
    public void setEnergy(int points) {
        energy += points;
        if (energy < 0)
            energy = 0;
        if (energy > 100)
            energy = 100;
    }

    /**
     * Checks the current energy
     * @return The energy value
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Updates the stomach meter
     * @param points The amount of points (+ or -) to adjust the value by
     */
    public void setStomach(int points) {
        stomach += points;
        if (stomach < 0)
            stomach = 0;
        if (stomach > 100)
            stomach = 100;
    }

    /**
     * Checks the current stomach level
     * @return The stomach value
     */
    public int getStomach() {
        return stomach;
    }

    /**
     * Gets the hitbox for the rock
     * @return The rectangle of collision
     */
    public Rectangle getColl() {
        return coll;
    }

    /**
     * Gets the rock ID used for idenitifying "favorites"
     * @return The rock ID
     */
    public int getFavoriteID() {return favoriteID;}
}
