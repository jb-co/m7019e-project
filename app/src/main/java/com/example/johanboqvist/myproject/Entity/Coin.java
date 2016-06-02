package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;

/**
 * Created by johanboqvist on 23/05/16.
 */
public class Coin extends Entity {

    public Coin(float x, float y) {
        super(x, y);

        this.frameDelay = 8;
    }

    @Override
    public void update(double delta) {
        this.frameCounter++;

        if(this.frameCounter > this.frameDelay) {
            this.frame = (this.frame + 1) % 4;
            this.frameCounter = 0;
        }
    }

    public Rect getFrame(){
        return new Rect(frame * 16, 6*16, frame * 16 + 16, 6*16 + 16);
    }

    @Override
    public void handleCollision() {

    }
    public boolean isCollectible(){return true; }
}
