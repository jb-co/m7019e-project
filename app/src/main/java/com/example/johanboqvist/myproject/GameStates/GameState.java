package com.example.johanboqvist.myproject.GameStates;

import android.graphics.Canvas;

import com.example.johanboqvist.myproject.SurfaceActivity;

/**
 * Created by johanboqvist on 23/05/16.
 */
public abstract class GameState {

    //change to gameManager?
    SurfaceActivity surfaceActivity;

    public GameState(SurfaceActivity surfaceActivity){
        this.surfaceActivity = surfaceActivity;
    }
    public abstract void render(Canvas canvas);
}
