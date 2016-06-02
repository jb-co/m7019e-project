package com.example.johanboqvist.myproject.Misc;

/**
 * This class holds important static variables used through out app.
 */
public class Globals {

    private static Globals instance = null;

    public static float     TILE_WIDTH;     //width of tile (based on screen size)
    public static float     TILE_HEIGHT;    //height of tile (based on screen size)
    public static int       CANVAS_WIDTH;   //set when surface is created
    public static int       CANVAS_HEIGHT;  //set when surface is created
    public static float     SCALE_WIDTH;    //Scale factor x (based on screen size)
    public static float     SCALE_HEIGHT;   //Scale factor y (based on screen size)
    public static boolean   backPressed = false; //Did the activity stop because of back press?
    public static boolean   homePressed = true; //Did the activity stop because of home press?

    protected Globals(){
    }

    public static Globals getInstance() {
        if(instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
