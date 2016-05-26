package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Created by johanboqvist on 20/05/16.
 */
public abstract class Mob extends Entity {

    protected int dir = 1;
    protected float prevX, prevY;
    protected float speed = 1;
    protected boolean isOOB = false;



    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setCurrentPos(){
        this.prevX = this.x;
        this.prevY = this.y;
    }

    public void loadCurrentPos(){
        this.x = this.prevX;
        this.y = this.prevY;
    }


    public abstract void update(double delta);

    public Mob(float x, float y){
        this.x = x;
        this.y = y;
    }

    public abstract Rect getFrame();

    public abstract void handleCollision();

    public void setOOB(boolean isOOB){
        this.isOOB = isOOB;
    }

    public boolean isOOB(){
        return this.isOOB;
    }

    public void checkOutOfBounds(float scrollX, float scrollY){
        //out of bounds check
        if(x - scrollX < -Globals.TILE_WIDTH || x - scrollX > Globals.CANVAS_WIDTH
                || y - scrollY < -Globals.TILE_HEIGHT || y - scrollY > Globals.CANVAS_HEIGHT){
            setOOB(true);
        } else {
            setOOB(false);
        }
    }
}
