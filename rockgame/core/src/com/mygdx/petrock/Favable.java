package com.mygdx.petrock;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Object used for representing toys and foods in the game.
 * These items lie in categories where a certain type of rock can
 * have a "favorite" in the category.
 */
public class Favable extends HoverButton {
    private int faveID;

    /**
     * Constructs a new toy or food item.
     * @param world The world entity, used for object generation purposed
     * @param imgPath The path of the image file for this item's sprite
     * @param x The x position of the item on-screen
     * @param y The y position of the item on-screen
     * @param id The ID number of this item. If it matches the rock's favorite,
     *           it will have a doubled effect when used.
     */
    public Favable(World world, String imgPath, int x, int y, int id) {
        super(world, imgPath, x, y);
        faveID = id;
    }

    /**
     * Checks the ID number for this item
     * @return The ID number
     */
    public int getID() {
        return faveID;
    }
}
