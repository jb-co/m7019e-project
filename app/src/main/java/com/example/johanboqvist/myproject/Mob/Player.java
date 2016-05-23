package com.example.johanboqvist.myproject.Mob;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.johanboqvist.myproject.SurfaceActivity;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Player extends Mob {

    private int offsetY = 1;

    public Player(float x, float y) {
        super(x, y);

        this.frameDelay = 10;
    }

    @Override
    public void update(double delta) {

        frameCounter++;

        if(frameCounter > frameDelay){
            frameCounter = 0;
            frame = (frame + 1) % 5;
        }

        if(dir == 1){
            offsetY = 4;
        } else {
            offsetY = 1;
        }
    }

    @Override
    public Rect getFrame() {

        return new Rect(frame * 16, 16*offsetY, frame * 16 + 16, 16*offsetY + 16);
    }

    public RectF getRect(){ return new RectF(x, y, x + SurfaceActivity.TILE_SIZE, y + SurfaceActivity.TILE_SIZE); }

    @Override
    public void handleCollision() {

    }

    public void setDir(int dir){
        this.dir = dir;
    }


}
