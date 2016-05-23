package com.example.johanboqvist.myproject.Mob;

import android.graphics.Rect;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Circler extends Mob {

    float rotation = 0;
    float centerX, centerY;
    int r = 200;

    public Circler(float x, float y) {
        super(x, y);

        centerX = x;
        centerY = y;
        this.speed = 50;
    }

    @Override
    public void update(double delta) {

        x = centerX + (float) Math.cos(rotation) * r;
        y = centerY + (float) Math.sin(rotation) * r;

        rotation += 0.05 * delta * this.speed;

        frameCounter++;

        if(frameCounter > frameDelay){
            frameCounter = 0;
            frame = (frame + 1) % 6;

        }
    }

    @Override
    public void handleCollision() {

    }

    public Rect getFrame() {
        return new Rect(frame * 16, 0, frame * 16 + 15, 16);
    }
}
