package com.example.johanboqvist.myproject;

import android.graphics.Rect;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Entity {

    public final static Entity floor = new Entity();

    private float x, y;

    public Rect getFrame() {
        return new Rect(0, 5*16, 16, 5*16 + 16);
    }
}
