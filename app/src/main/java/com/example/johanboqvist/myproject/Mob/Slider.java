package com.example.johanboqvist.myproject.Mob;

import android.graphics.Rect;

import com.example.johanboqvist.myproject.MapManager;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Slider extends Mob {


    public Slider(float x, float y) {
        super(x, y);
    }

    @Override
    public Rect getFrame() {
            return new Rect(frame * 16, 16*2, frame * 16 + 16, 16*2 + 16);
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

        frameCounter++;

        if(frameCounter > frameDelay) {
            frameCounter = 0;
            frame += 1;

            frame = frame % 6;
        }

    }



}
