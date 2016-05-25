package com.example.johanboqvist.myproject.Entity;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.johanboqvist.myproject.Misc.Globals;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Entity {

    protected float x, y;
    protected int frame = 0;
    protected int frameDelay = 8;
    protected int frameCounter = 0;
    protected boolean isDead = false;

    public Rect getFrame() {
        return new Rect(0, 5*16, 16, 5*16 + 16);
    }
    public RectF getRect(float scrollX, float scrollY){ return new RectF(x - scrollX, y - scrollY,
            x + Globals.TILE_WIDTH - scrollX , y + Globals.TILE_HEIGHT - scrollY); }

    public boolean isCollectible(){return false; }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
