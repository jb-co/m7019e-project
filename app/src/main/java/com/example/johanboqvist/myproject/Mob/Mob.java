package com.example.johanboqvist.myproject.Mob;

import android.graphics.Paint;

import com.example.johanboqvist.myproject.GameActivity;
import com.example.johanboqvist.myproject.MapManager;

/**
 * Created by johanboqvist on 20/05/16.
 */
public abstract class Mob {

    protected float x, y;
    protected int dir = 1;
    protected MapManager mapManager;
    protected float prevX, prevY;

    protected float speed = 1;
    private Paint paint;


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


    public abstract void move();

    public Mob(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public Paint getPaint() {
        return paint;
    }


    public abstract void handleCollision();
}
