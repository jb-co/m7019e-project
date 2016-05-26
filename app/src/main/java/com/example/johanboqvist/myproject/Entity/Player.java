package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Player extends Mob {

    private int offsetY = 1;
    private float startX, startY;


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

    public RectF getRect(){ return new RectF(x, y, x + Globals.TILE_WIDTH, y + Globals.TILE_HEIGHT); }

    @Override
    public void handleCollision() {

    }

    public void setStartPos(float x, float y){
        this.startX = x;
        this.startY = y;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setDir(int dir){
        this.dir = dir;
    }


}
