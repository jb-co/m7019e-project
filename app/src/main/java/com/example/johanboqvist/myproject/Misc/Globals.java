package com.example.johanboqvist.myproject.Misc;

/**
 * Created by johanboqvist on 24/05/16.
 */
public class Globals {

    private static Globals instance = null;

    public static float     TILE_WIDTH;
    public static float     TILE_HEIGHT;
    public static int       CANVAS_WIDTH;
    public static int       CANVAS_HEIGHT;
    public static float     SCALE_WIDTH;
    public static float     SCALE_HEIGHT;
    public static boolean   backPressed = false;
    public static boolean   homePressed = true;

    protected Globals(){
    }

    public static Globals getInstance() {
        if(instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
