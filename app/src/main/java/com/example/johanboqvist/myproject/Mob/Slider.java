package com.example.johanboqvist.myproject.Mob;

import com.example.johanboqvist.myproject.MapManager;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Slider extends Mob {


    public Slider(float x, float y) {
        super(x, y);
    }

    @Override
    public void handleCollision() {

        this.dir *= -1;
        loadCurrentPos();
    }

    @Override
    public void move() {

        setCurrentPos();

        this.y += 1.5 * this.dir;

    }



}
