package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Entity class. An entity is a player/enemy/item etc.
 */
public abstract class Entity {

    protected float     x, y;               //position
    protected int       frame = 0;
    protected int       frameDelay = 8;       //animation speed
    protected int       frameCounter = 0;
    protected int       dir = 1;
    protected float     prevX, prevY;
    protected float     speed = 1;
    protected boolean   isOOB = false;

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Get the rectangle on the spritesheet where to crop.
     * @return
     */
    public abstract Rect getFrame();


    /**
     * Get the rectangle of the entity on the actual map, for collision.
     * @param scrollX Amount of screen scroll x
     * @param scrollY Amount of screen scroll y
     * @return
     */
    public RectF getRect(float scrollX, float scrollY){ return new RectF(x - scrollX, y - scrollY,
            x + Globals.TILE_WIDTH - scrollX , y + Globals.TILE_HEIGHT - scrollY); }

    /**
     * Check if the entity is out of bounds. Sets isOOB variable.
     * @param scrollX Screen scroll x
     * @param scrollY Screen scroll y
     */
    public void checkOutOfBounds(float scrollX, float scrollY){
        //out of bounds check
        if(x - scrollX < -Globals.TILE_WIDTH || x - scrollX > Globals.CANVAS_WIDTH
                || y - scrollY < -Globals.TILE_HEIGHT || y - scrollY > Globals.CANVAS_HEIGHT){
            setOOB(true);
        } else {
            setOOB(false);
        }
    }

    public boolean isCollectible(){return false; }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }


    public void setCurrentPos(){
        this.prevX = this.x;
        this.prevY = this.y;
    }

    public void loadCurrentPos(){
        this.x = this.prevX;
        this.y = this.prevY;
    }

    public abstract void handleCollision();
    public abstract void update(double delta);


    public void setOOB(boolean isOOB){
        this.isOOB = isOOB;
    }

    public boolean isOOB(){
        return this.isOOB;
    }
}
