package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 *  Entity Circler. Bird which circles with radius r.
 */
public class Circler extends Entity {

    private float   rotation = 0;
    private float   centerX, centerY;
    private int     r;

    public Circler(float x, float y) {
        super(x, y);

        centerX = x;
        centerY = y;
        this.speed = 50;
        r = 200;
    }

    @Override
    public void update(double delta) {

        x = centerX + (float) Math.cos(rotation) * r * Globals.SCALE_WIDTH;
        y = centerY + (float) Math.sin(rotation) * r * Globals.SCALE_HEIGHT;

        rotation += 0.05 * delta * this.speed;

        frameCounter++;

        if(frameCounter > frameDelay){
            frameCounter = 0;
            frame = (frame + 1) % 6;

        }
    }

    public void handleCollision() {

    }

    public Rect getFrame() {
        return new Rect(frame * 16, 0, frame * 16 + 15, 16);
    }
}
