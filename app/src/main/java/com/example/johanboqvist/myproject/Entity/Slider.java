package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Slider extends Entity {


    private int ori; //up-down = 0 or left-right = 1
    private float speedX, speedY;

    public Slider(float x, float y, int ori) {
        super(x, y);

        this.speedX = 172f * Globals.SCALE_WIDTH;
        this.speedY = 172f * Globals.SCALE_HEIGHT;
        this.ori = ori;
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

        if(ori == 0) {
            this.y += 1.5 * this.dir * delta * this.speedY;
        } else {
            this.x += 1.5 * this.dir * delta * this.speedX;
        }

        frameCounter++;

        if(frameCounter > frameDelay || frameCounter < 0) {
            frameCounter = 0;
            frame += this.dir;

            frame = frame % 6;

            if(frame < 0) frame = 5;

        }

    }



}
