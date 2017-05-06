package com.mygdx.petrock;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Mike on 2/18/2017.
 */
public class Poop extends HoverButton {
    public Poop(World world, String imgPath, int x, int y) {
        super(world, imgPath, x, y);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer renderer) {
        this.getSprite().setPosition(this.getBody().getPosition().x, this.getBody().getPosition().y);
        this.getSprite().draw(batch);
        renderer.setColor(Color.WHITE);
        renderer.rect(this.getBody().getPosition().x, this.getBody().getPosition().y+64, this.getHovermeter()*4,10);
    }
}
