package com.example.johanboqvist.myproject;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Entity {


    protected float x, y;
    protected int frame = 0;
    protected int frameDelay = 8;
    protected int frameCounter = 0;

    public Rect getFrame() {
        return new Rect(0, 5*16, 16, 5*16 + 16);
    }
    public RectF getRect(float scrollX, float scrollY){ return new RectF(x - scrollX, y - scrollY,
            x + GameActivity.TILE_SIZE - scrollX , y + GameActivity.TILE_SIZE - scrollY); }

    public boolean isCollectible(){return false; }
}
