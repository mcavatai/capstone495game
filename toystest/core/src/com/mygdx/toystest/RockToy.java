package com.mygdx.toystest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class RockToy {
    private Sprite sprite;
    private Body body;

    protected int hovermeter;
    protected Rectangle coll;

    protected int toyID;

    public RockToy(World world, int toyID, String imgPath, int x, int y) {
        BodyDef rockToy = new BodyDef();
        rockToy.type = BodyDef.BodyType.StaticBody;
        rockToy.position.set(x, y);
        body = world.createBody(rockToy);
        coll = new Rectangle();
        coll.set(rockToy.position.x, rockToy.position.y, 64, 64);
        sprite = new Sprite(new Texture(Gdx.files.internal(imgPath)));
        sprite.setSize(64,64);
        body.setUserData(sprite);

        this.toyID = toyID;
        hovermeter = 0;
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
