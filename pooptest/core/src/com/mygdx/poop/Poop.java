package com.mygdx.poop;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Mike on 2/4/2017.
 */
public class Poop extends Rectangle {
    private long poopTimer;

    public Poop (long time) {
        this.poopTimer = time;
    }

    public void setPoopTimer(long time) {
        poopTimer = time;
    }

    public long getPoopTimer() {
        return poopTimer;
    }
}
