package com.mygdx.petrock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * General class for any objects that require hovering over with the mouse
 */
public class HoverButton {
    private Sprite sprite;
    private Body body;

    private int hovermeter;
    private Rectangle coll;

    public HoverButton(World world, String imgPath, int x, int y) {
        BodyDef buttonDef = new BodyDef();
        buttonDef.type = BodyDef.BodyType.StaticBody;
        buttonDef.position.set(x, y);
        body = world.createBody(buttonDef);

        coll = new Rectangle(body.getPosition().x, body.getPosition().y, 64, 64);

        sprite = new Sprite(new Texture(Gdx.files.internal(imgPath)));
        sprite.setSize(64,64);
        body.setUserData(sprite);

        hovermeter = 0;
    }

    public void render(SpriteBatch batch, ShapeRenderer renderer) {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
        renderer.setColor(Color.WHITE);
        renderer.rect(body.getPosition().x, body.getPosition().y+64, hovermeter,10);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void incrementHovermeter() {
        hovermeter++;
    }

    public void setHovermeter(int n) {
        hovermeter = n;
    }

    public int getHovermeter() {
        return hovermeter;
    }

    public Rectangle getColl() {
        return coll;
    }

    public Body getBody() {
        return body;
    }
}
