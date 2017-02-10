package com.mygdx.toystest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Rock {
    private Sprite sprite;
    private Body body;

    protected Rectangle coll;

    protected int happy;
    protected int energy;
    protected int toyID;

    public Rock(World world, int toyID) {
        BodyDef rockDef = new BodyDef();
        rockDef.type = BodyDef.BodyType.StaticBody;
        rockDef.position.set(640/2 - 32, 480/2 - 32);
        body = world.createBody(rockDef);
        coll = new Rectangle();
        coll.set(rockDef.position.x, rockDef.position.y, 64, 64);
        sprite = new Sprite(new Texture(Gdx.files.internal("rock.png")));
        sprite.setSize(64,64);
        body.setUserData(sprite);

        this.toyID = toyID;

        happy = 50;
        energy = 100;
    }

    public void render(SpriteBatch batch) {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
