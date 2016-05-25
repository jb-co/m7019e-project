package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Slider extends Mob {


    public Slider(float x, float y) {
        super(x, y);

        this.speed = 172f * Globals.SCALE_HEIGHT;
    }

    @Override
    public Rect getFrame() {
            return new Rect(frame * 16, 16*3, frame * 16 + 16, 16*3 + 16);
    }

    @Override
    public void handleCollision() {

        this.dir *= -1;
        loadCurrentPos();
    }

    @Override
    public void update(double delta) {

        setCurrentPos();

        this.y += 1.5 * this.dir * delta * this.speed;

        frameCounter++;

        if(frameCounter > frameDelay || frameCounter < 0) {
            frameCounter = 0;
            frame += this.dir;

            frame = frame % 6;

            if(frame < 0) frame = 5;

        }

    }



}
